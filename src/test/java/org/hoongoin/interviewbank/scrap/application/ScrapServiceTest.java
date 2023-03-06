package org.hoongoin.interviewbank.scrap.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.hoongoin.interviewbank.interview.domain.InterviewQueryService;
import org.hoongoin.interviewbank.interview.domain.QuestionQueryService;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapAndScrapQuestions;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionWithScrapAnswers;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
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
    private QuestionQueryService questionQueryService;
    @Mock
    private AccountQueryService accountQueryService;
    @Mock
    private ScrapAnswerCommandService scrapAnswerCommandService;
    @Mock
    private ScrapQuestionCommandService scrapQuestionCommandService;
    @Mock
    private ScrapQuestionQueryService scrapQuestionQueryService;
    @Mock
    private ScrapMapper scrapMapper;

    @Test
    void createScrapByCreateRequest_Success() {
        //given
        CreateScrapRequest createScrapRequest = new CreateScrapRequest(1);

        Interview originalInterview = Interview.builder().interviewId(1L).title("title").accountId(2L)
                .createdAt(LocalDateTime.now()).deletedFlag(false).build();
        given(interviewQueryService.findInterviewById(1)).willReturn(originalInterview);

        Question question1 = Question.builder().questionId(1L).interviewId(1L).content("question 1")
                .createdAt(LocalDateTime.now()).deletedFlag(false).build();
        Question question2 = Question.builder().questionId(2L).interviewId(1L).content("question 2")
                .createdAt(LocalDateTime.now()).deletedFlag(false).build();
        List<Question> originalQuestionsInInterview = new ArrayList<>();
        originalQuestionsInInterview.add(question1);
        originalQuestionsInInterview.add(question2);
        given(questionQueryService.findQuestionsByInterviewId(1)).willReturn(originalQuestionsInInterview);

        Account requestingAccount = Account.builder().accountId(10).nickname("ra")
                .email("requesting@test.com").password("requesting").createdAt(LocalDateTime.now()).deletedFlag(false)
                .build();
        given(accountQueryService.findAccountByAccountId(10)).willReturn(requestingAccount);

        Scrap scrap = Scrap.builder().scrapId(1).accountId(10).interviewId(1).title(originalInterview.getTitle())
                .createdAt(LocalDateTime.now()).build();
        ScrapQuestion scrapQuestion1 = ScrapQuestion.builder().scrapQuestionId(1).scrapId(1)
                .content(question1.getContent()).createdAt(LocalDateTime.now()).build();
        ScrapQuestion scrapQuestion2 = ScrapQuestion.builder().scrapQuestionId(2).scrapId(1)
                .content(question1.getContent()).createdAt(LocalDateTime.now()).build();
        List<ScrapQuestion> scrapQuestions = new ArrayList<>();
        scrapQuestions.add(scrapQuestion1);
        scrapQuestions.add(scrapQuestion2);
        ScrapAndScrapQuestions savedScrapAndScrapQuestions = new ScrapAndScrapQuestions(scrap, scrapQuestions);
        given(scrapCommandService.insertScrapAndScrapQuestions(any(), any(), any(), any()))
                .willReturn(savedScrapAndScrapQuestions);
        given(scrapMapper.scrapToScrapResponse(scrap)).willReturn(new ScrapResponse(scrap.getScrapId(), scrap.getTitle()));
        given(scrapMapper.scrapQuestionToScrapQuestionResponse(scrapQuestion1)).willReturn(new ScrapQuestionResponse(scrapQuestion1.getScrapQuestionId(), scrapQuestion1.getContent()));
        given(scrapMapper.scrapQuestionToScrapQuestionResponse(scrapQuestion2)).willReturn(new ScrapQuestionResponse(scrapQuestion2.getScrapQuestionId(), scrapQuestion2.getContent()));

        //when
        CreateScrapResponse createScrapResponse = scrapService.createScrapByCreateRequest(createScrapRequest, 10);

        //then
        assertThat(createScrapResponse.getOriginalInterview().getInterviewId()).isEqualTo(originalInterview.getInterviewId());
        assertThat(createScrapResponse.getScrap().getScrapId()).isEqualTo(scrap.getScrapId());
        assertThat(createScrapResponse.getScrap().getTitle()).isEqualTo(scrap.getTitle());
        assertThat(createScrapResponse.getScrapQuestions().size()).isEqualTo(scrapQuestions.size());
        assertThat(createScrapResponse.getScrapQuestions().get(0).getScrapQuestionId()).isEqualTo(scrapQuestions.get(0).getScrapQuestionId());
        assertThat(createScrapResponse.getScrapQuestions().get(0).getContent()).isEqualTo(scrapQuestions.get(0).getContent());
    }

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
        UpdateScrapResponse updateScrapResponse = scrapService.updateScrapByRequestAndScrapId(updateScrapRequest, scrapId, requestingAccountId);

        //then
        assertThat(updateScrapResponse.getTitle()).isEqualTo(scrap.getTitle());
    }

    @Test
    void deleteScrapByRequestAndScrapId_Success(){
        //given
        long scrapId = 1;
        long requestingAccountId = 1;
        Scrap scrap = Scrap.builder().scrapId(scrapId).accountId(requestingAccountId).interviewId(1).title("title").build();

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
    void readScrapDetailById_Success(){
        // given
        long scrapId = 1L;
        long requestingAccountId = 2L;

        Scrap scrap = Scrap.builder().scrapId(scrapId).title("title").build();
        scrap.setAccountId(requestingAccountId);
        Interview interview = Interview.builder().interviewId(3L).title("title").build();
        List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers = Arrays.asList(ScrapQuestionWithScrapAnswers.builder().build());

        given(scrapQueryService.findScrapByScrapId(scrapId)).willReturn(scrap);
        given(interviewQueryService.findInterviewById(scrap.getInterviewId())).willReturn(interview);
        given(scrapQuestionQueryService.findAllScrapQuestionWithScrapAnswersByScrapId(scrapId)).willReturn(scrapQuestionsWithScrapAnswers);
        given(scrapMapper.scrapToScrapResponse(scrap)).willReturn(new ScrapResponse(scrap.getScrapId(), scrap.getTitle()));

        List<ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersResponses = Arrays.asList(new ScrapQuestionWithScrapAnswersResponse());
        given(scrapMapper.scrapQuestionWithScrapAnswersToScrapQuestionWithScrapAnswersResponse(scrapQuestionsWithScrapAnswers.get(0))).willReturn(scrapQuestionWithScrapAnswersResponses.get(0));

        // when
        ReadScrapDetailResponse response = scrapService.readScrapDetailById(scrapId, requestingAccountId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getScrap().getScrapId()).isEqualTo(scrapId);
        assertThat(response.getOriginalInterview().getInterviewId()).isEqualTo(interview.getInterviewId());
        assertThat(response.getScrapQuestionWithScrapAnswersList().get(0)).isEqualTo(scrapQuestionWithScrapAnswersResponses.get(0));
    }
}
