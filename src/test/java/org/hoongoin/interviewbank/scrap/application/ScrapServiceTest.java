package org.hoongoin.interviewbank.scrap.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.domain.InterviewQueryService;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.ScrapTestFactory;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionWithScrapAnswers;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.*;
import org.hoongoin.interviewbank.scrap.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScrapServiceTest {

	@InjectMocks
	private ScrapService scrapService;
	@Mock
	private ScrapQueryService scrapQueryService;
	@Mock
	private ScrapCommandService scrapCommandService;
	@Mock
	private InterviewQueryService interviewQueryService;
	@Mock
	private ScrapAnswerCommandService scrapAnswerCommandService;
	@Mock
	private ScrapQuestionCommandService scrapQuestionCommandService;
	@Mock
	private ScrapQuestionQueryService scrapQuestionQueryService;
	@Mock
	private ScrapMapper scrapMapper;

	@Test
	void updateScrapByRequestAndScrapId_Success() {
		//given
		long scrapId = 1;
		long requestingAccountId = 1;
		Scrap scrap = ScrapTestFactory.createScrap();
		UpdateScrapRequest updateScrapRequest = new UpdateScrapRequest("New Title", false);
		Scrap updatedScrap = Scrap.builder().scrapId(1).accountId(1).interviewId(1).title(updateScrapRequest.getTitle())
			.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

		given(scrapQueryService.findScrapByScrapId(1)).willReturn(scrap);

		given(scrapCommandService.updateScrap(scrap)).willReturn(updatedScrap);

		given(scrapMapper.scrapToUpdateScrapResponse(updatedScrap)).willReturn(
			new UpdateScrapResponse(updateScrapRequest.getTitle()));

		//when
		UpdateScrapResponse updateScrapResponse = scrapService.updateScrapByRequestAndScrapId(updateScrapRequest,
			scrapId, requestingAccountId);

		//then
		assertThat(updateScrapResponse.getTitle()).isEqualTo(updateScrapRequest.getTitle());
	}

	@Test
	void deleteScrapByRequestAndScrapId_Success() {
		//given
		long scrapId = 1;
		long requestingAccountId = 1;
		Scrap scrap = ScrapTestFactory.createScrap();

		given(scrapQueryService.findScrapByScrapId(scrapId)).willReturn(scrap);

		// when
		scrapService.deleteScrapByRequestAndScrapId(scrapId, requestingAccountId);

		// Then
		verify(scrapAnswerCommandService).deleteAllScrapAnswerByScrapId(scrapId);
		verify(scrapQuestionCommandService).deleteAllScrapQuestionByScrapId(scrapId);
		verify(scrapCommandService).deleteScrapById(scrapId);
		assertThat(scrap.getAccountId()).isEqualTo(requestingAccountId);
	}

	@Test
	void readScrapDetailById_Success() {
		// given
		long scrapId = 1L;
		long requestingAccountId = 2L;

		Scrap scrap = ScrapTestFactory.createScrap();
		scrap.setAccountId(requestingAccountId);
		Interview interview = InterviewTestFactory.createInterview(requestingAccountId);
		List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers = List.of(
			ScrapQuestionWithScrapAnswers.builder().build());

		given(scrapQueryService.findScrapByScrapId(scrapId)).willReturn(scrap);
		given(interviewQueryService.findInterviewById(scrap.getInterviewId())).willReturn(interview);
		given(scrapQuestionQueryService.findAllScrapQuestionWithScrapAnswersByScrapId(scrapId)).willReturn(
			scrapQuestionsWithScrapAnswers);
		given(scrapMapper.scrapToReadScrapDetailResponseOfScrapResponse(scrap)).willReturn(
			new ReadScrapDetailResponse.ScrapResponse(scrap.getScrapId(), scrap.getTitle(), LocalDate.now()));

		List<ReadScrapDetailResponse.ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersResponses = List.of(
			new ReadScrapDetailResponse.ScrapQuestionWithScrapAnswersResponse());
		given(scrapMapper.scrapQuestionWithScrapAnswersToScrapQuestionWithScrapAnswersResponse(
			scrapQuestionsWithScrapAnswers.get(0))).willReturn(scrapQuestionWithScrapAnswersResponses.get(0));

		// when
		ReadScrapDetailResponse response = scrapService.readScrapDetailById(scrapId, requestingAccountId);

		// then
		assertThat(response).isNotNull();
		assertThat(response.getScrap().getScrapId()).isEqualTo(scrapId);
		assertThat(response.getOriginalInterview().getInterviewId()).isEqualTo(interview.getInterviewId());
		assertThat(response.getScrapQuestionWithScrapAnswersList().get(0)).isEqualTo(
			scrapQuestionWithScrapAnswersResponses.get(0));
	}

	@Test
	void readScrapDetailById_Fail_WhenReadingPrivateScrapOfAnotherUser(){
		// given
		long scrapId = 1L;
		long scrapWriterAccountId = 1L;
		long requestingAccountId = 2L;

		Scrap scrap = ScrapTestFactory.createScrap();

		given(scrapQueryService.findScrapByScrapId(scrapId)).willReturn(scrap);

		// when //then
		assertThatThrownBy(() -> {
			scrapService.readScrapDetailById(scrapId, requestingAccountId);
		}).isInstanceOf(IbBadRequestException.class)
			.hasMessage("Bad Request");
	}
}
