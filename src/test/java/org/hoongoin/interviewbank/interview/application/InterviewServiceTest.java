package org.hoongoin.interviewbank.interview.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.response.DeleteInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.domain.QuestionQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
@Sql(scripts = {"classpath:/job-category-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class InterviewServiceTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private QuestionQueryService questionQueryService;

	@Autowired
	private InterviewService interviewService;

	@Test
	void createInterviewAndQuestionsByRequest_Success() {
		//given
		int questionSize = 2;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			2);

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		//then
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).extracting("content")
			.containsExactlyInAnyOrder("content1",
				"content2");
		assertThat(createInterviewAndQuestionsResponse.getTitle()).isEqualTo(
			createInterviewAndQuestionsRequest.getTitle());
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).hasSize(questionSize);
		assertThat(createInterviewAndQuestionsResponse.getInterviewId()).isNotNull();
	}

	@Test
	void createInterviewAndQuestionsByRequest_Fail_QuestionSizeIs1001() {
		//given
		int questionSize = 1001;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		//when //then
		assertThatThrownBy(
			() -> interviewService.createInterviewAndQuestionsByRequest(createInterviewAndQuestionsRequest,
				testAccountEntity.getId())).hasMessage("Question Size Validation Failed")
			.isInstanceOf(IbValidationException.class);
	}

	@Test
	void createInterviewAndQuestionsByRequest_Success_QuestionSizeIs1000() {
		//given
		int questionSize = 1000;
		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		//then
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).hasSize(1000);
	}

	@Test
	void findInterviewPageByPageAndSize_Success_InterviewSoftDeleteSize() {
		//given
		int questionSize = 1000;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		Long interviewId = createInterviewAndQuestionsResponse.getInterviewId();

		interviewService.deleteInterviewById(interviewId, testAccountEntity.getId());

		//when
		FindInterviewPageResponse interviewPageByPageAndSize = interviewService.findInterviewPageByPageAndSize(0, 1);

		//then
		assertThat(interviewPageByPageAndSize.getInterviews()).isEmpty();
	}

	@Test
	void findInterviewPageByPageAndSize_Success_QuestionSoftDeleteSize() {
		//given
		int questionSize = 1000;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		Long interviewId = createInterviewAndQuestionsResponse.getInterviewId();

		DeleteInterviewResponse deleteInterviewResponse = interviewService.deleteInterviewById(interviewId,
			testAccountEntity.getId());

		//when
		List<Question> deletedQuestions = questionQueryService.findQuestionsByInterviewId(
			deleteInterviewResponse.getInterviewId());

		//then
		assertThat(deletedQuestions).isEmpty();

	}

	@Test
	void findInterviewsByAccountId_Success_Page1() {
		//given
		int questionSize = 2;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse2 = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());
		//when
		FindInterviewPageResponse findMyInterviewResponse = interviewService.findInterviewsByAccountId(
			testAccountEntity.getId(), 0, 10);

		//then
		assertThat(findMyInterviewResponse.getInterviews()).hasSize(2);
		assertThat(findMyInterviewResponse.getInterviews().get(0).getTitle()).isEqualTo(
			createInterviewAndQuestionsResponse.getTitle());
	}

	@Test
	void findInterviewsByAccountId_Success_Delete() {
		//given
		int questionSize = 2;

		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse2 = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());
		interviewService.deleteInterviewById(createInterviewAndQuestionsResponse2.getInterviewId(),
			testAccountEntity.getId());
		//when
		FindInterviewPageResponse findMyInterviewResponse = interviewService.findInterviewsByAccountId(
			testAccountEntity.getId(), 0, 10);

		//then
		assertThat(findMyInterviewResponse.getInterviews()).hasSize(1);
		assertThat(findMyInterviewResponse.getInterviews().get(0).getInterviewId()).isEqualTo(
			createInterviewAndQuestionsResponse.getInterviewId());
	}
}
