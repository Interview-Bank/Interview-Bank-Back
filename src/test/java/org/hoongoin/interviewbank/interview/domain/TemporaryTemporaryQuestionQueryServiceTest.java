package org.hoongoin.interviewbank.interview.domain;

import static org.assertj.core.api.Assertions.*;

import javax.transaction.Transactional;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.application.InterviewService;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

@Transactional
@IbSpringBootTest
@Sql(scripts = {"classpath:/account-data.sql",
	"classpath:/job-category-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TemporaryTemporaryQuestionQueryServiceTest {

	@Autowired
	private QuestionQueryService questionQueryService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewService interviewService;

	@Test
	void findQuestionsByInterviewId_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		int questionSize = 2;

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

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
}
