package org.hoongoin.interviewbank.interview.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.common.gpt.GptRequestHandler;
import org.hoongoin.interviewbank.common.gpt.GptResponseBody;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.gpt.GptResponseBodyFactory;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
@Sql(scripts = {"classpath:/account-data.sql",
	"classpath:/job-category-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class InterviewControllerTest {

	@Autowired
	private InterviewController interviewController;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@MockBean
	private GptRequestHandler gptRequestHandler;

	private static final String testTitle = "title";
	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void createInterview_Success() {
		//given
		int questionSize = 2;

		GptResponseBody gptResponseBody = GptResponseBodyFactory.createMockGptResponseBody();
		given(gptRequestHandler.sendChatCompletionRequest(any())).willReturn(gptResponseBody);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		//when
		ResponseEntity<CreateInterviewAndQuestionsResponse> createInterviewAndQuestionsResponse = interviewController.createInterviewAndQuestions(
			createInterviewAndQuestionsRequest);

		//then
		assertThat(createInterviewAndQuestionsResponse.getBody().getTitle()).isEqualTo(testTitle);
		assertThat(createInterviewAndQuestionsResponse.getBody().getQuestions()).hasSize(2);
	}

	@Test
	void updateInterview_Success() {
		//given
		AccountEntity savedAccount = AccountTestFactory.createAccountEntity();

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(testTitle, savedAccount.getId());

		InterviewEntity interviewEntity = InterviewEntity.builder()
			.title(createInterviewRequest.getTitle())
			.accountEntity(savedAccount)
			.build();
		InterviewEntity savedInterviewEntity = interviewRepository.saveAndFlush(interviewEntity);

		String updatedTitle = "updateTitle";

		QuestionEntity question1 = QuestionEntity.builder()
			.content("content")
			.interviewEntity(savedInterviewEntity)
			.build();
		QuestionEntity question2 = QuestionEntity.builder()
			.content("content2")
			.interviewEntity(savedInterviewEntity)
			.build();

		QuestionEntity savedQuestion1 = questionRepository.saveAndFlush(question1);
		QuestionEntity savedQuestion2 = questionRepository.saveAndFlush(question2);

		UpdateInterviewRequest.Question newQuestion1 = new UpdateInterviewRequest.Question("newContent1",
			savedQuestion1.getId());
		UpdateInterviewRequest.Question newQuestion2 = new UpdateInterviewRequest.Question("newContent2",
			savedQuestion2.getId());

		List<UpdateInterviewRequest.Question> questions = new ArrayList<>();

		questions.add(newQuestion1);
		questions.add(newQuestion2);

		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(questions, updatedTitle, 1L,
			InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR);

		GptResponseBody gptResponseBody = GptResponseBodyFactory.createMockGptResponseBody();
		given(gptRequestHandler.sendChatCompletionRequest(any())).willReturn(gptResponseBody);

		//when
		ResponseEntity<UpdateInterviewResponse> updateInterviewResponse = interviewController.updateInterview(
			updateInterviewRequest, savedInterviewEntity.getId());

		//then
		assertThat(updateInterviewResponse.getBody().getTitle()).isEqualTo(updatedTitle);
		assertThat(updateInterviewResponse.getBody().getQuestions()).extracting("content")
			.containsExactlyInAnyOrder("newContent1", "newContent2");
	}

	@Test
	void findInterview_Success() {
		//given
		int questionSize = 2;

		GptResponseBody gptResponseBody = GptResponseBodyFactory.createMockGptResponseBody();
		given(gptRequestHandler.sendChatCompletionRequest(any())).willReturn(gptResponseBody);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		ResponseEntity<CreateInterviewAndQuestionsResponse> createInterviewAndQuestionsResponse = interviewController.createInterviewAndQuestions(
			createInterviewAndQuestionsRequest);

		//when
		ResponseEntity<FindInterviewResponse> findQuestionsByInterviewIdResponse = interviewController.findInterview(
			createInterviewAndQuestionsResponse.getBody().getInterviewId());

		//then
		assertThat(findQuestionsByInterviewIdResponse.getBody().getInterviewId()).isEqualTo(
			createInterviewAndQuestionsResponse.getBody().getInterviewId());
		assertThat(findQuestionsByInterviewIdResponse.getBody().getQuestions()).extracting("content")
			.containsExactlyInAnyOrder(
				createInterviewAndQuestionsRequest.getQuestionsRequest().getQuestions().get(0).getContent(),
				createInterviewAndQuestionsRequest.getQuestionsRequest().getQuestions().get(1).getContent());
	}

	@Test
	void findInterviewPage_Success() {
		//given
		int questionSize = 2;

		GptResponseBody gptResponseBody = GptResponseBodyFactory.createMockGptResponseBody();
		given(gptRequestHandler.sendChatCompletionRequest(any())).willReturn(gptResponseBody);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		ResponseEntity<CreateInterviewAndQuestionsResponse> createInterviewAndQuestionsResponse = interviewController.createInterviewAndQuestions(
			createInterviewAndQuestionsRequest);

		//when
		ResponseEntity<FindInterviewPageResponse> interviewPage = interviewController.findInterviewPage(0, 5);

		//then
		assertThat(interviewPage.getBody().getInterviews()).hasSize(1);
		interviewPage.getBody()
			.getInterviews()
			.forEach(interview -> assertThat(interview.getNickname()).isEqualTo(testNickname));
	}
}
