package org.hoongoin.interviewbank.scrap.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import org.hoongoin.interviewbank.interview.domain.InterviewQueryService;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
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
		Scrap scrap = Scrap.builder().scrapId(1).accountId(1).interviewId(1).title("title")
			.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
		UpdateScrapRequest updateScrapRequest = new UpdateScrapRequest("New Title");

		given(scrapQueryService.findScrapByScrapId(1)).willReturn(scrap);

		Scrap scrapToUpdate = Scrap.builder().title(updateScrapRequest.getTitle()).build();
		given(scrapMapper.updateScrapRequestToScrap(updateScrapRequest)).willReturn(scrapToUpdate);

		scrap.setTitle(updateScrapRequest.getTitle());
		given(scrapCommandService.updateScrap(scrapId, scrapToUpdate)).willReturn(scrap);

		given(scrapMapper.scrapToUpdateScrapResponse(scrap)).willReturn(new UpdateScrapResponse(scrap.getTitle()));

		//when
		UpdateScrapResponse updateScrapResponse = scrapService.updateScrapByRequestAndScrapId(updateScrapRequest,
			scrapId, requestingAccountId);

		//then
		assertThat(updateScrapResponse.getTitle()).isEqualTo(scrap.getTitle());
	}

	@Test
	void deleteScrapByRequestAndScrapId_Success() {
		//given
		long scrapId = 1;
		long requestingAccountId = 1;
		Scrap scrap = Scrap.builder()
			.scrapId(scrapId)
			.accountId(requestingAccountId)
			.interviewId(1)
			.title("title")
			.build();

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

		Scrap scrap = Scrap.builder().scrapId(scrapId).title("title").build();
		scrap.setAccountId(requestingAccountId);
		Interview interview = Interview.builder().interviewId(3L).jobCategoryId(1L).title("title").build();
		List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers = Arrays.asList(
			ScrapQuestionWithScrapAnswers.builder().build());

		given(scrapQueryService.findScrapByScrapId(scrapId)).willReturn(scrap);
		given(interviewQueryService.findInterviewById(scrap.getInterviewId())).willReturn(interview);
		given(scrapQuestionQueryService.findAllScrapQuestionWithScrapAnswersByScrapId(scrapId)).willReturn(
			scrapQuestionsWithScrapAnswers);
		given(scrapMapper.scrapToScrapResponse(scrap)).willReturn(
			new ScrapResponse(scrap.getScrapId(), scrap.getTitle(), LocalDate.now()));

		List<ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersResponses = Arrays.asList(
			new ScrapQuestionWithScrapAnswersResponse());
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
}
