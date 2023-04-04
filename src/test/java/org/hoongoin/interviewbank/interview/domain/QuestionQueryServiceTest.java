package org.hoongoin.interviewbank.interview.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.application.InterviewService;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
@IbSpringBootTest
class QuestionQueryServiceTest {

	@Autowired
	private QuestionQueryService questionQueryService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewService interviewService;

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void findQuestionsByInterviewId_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		QuestionsRequest.Question question1 = new QuestionsRequest.Question("content1");
		QuestionsRequest.Question question2 = new QuestionsRequest.Question("content2");

		List<QuestionsRequest.Question> questions = new ArrayList<>();

		questions.add(question1);
		questions.add(question2);

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = new CreateInterviewAndQuestionsRequest(
			title, null, InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR, questionsRequest);

		interviewService.createInterviewAndQuestionsByRequest(createInterviewAndQuestionsRequest,
			testAccountEntity.getId());

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		//then
		assertThat(questionQueryService.findQuestionsByInterviewId(
			createInterviewAndQuestionsResponse.getInterviewId())).hasSameSizeAs(
			createInterviewAndQuestionsResponse.getQuestions());
	}

	private AccountEntity createTestAccountEntity() {
		return AccountEntity.builder()
			.id(testAccountId)
			.password(testPassword)
			.email(testEmail)
			.nickname(testNickname)
			.build();
	}
}
