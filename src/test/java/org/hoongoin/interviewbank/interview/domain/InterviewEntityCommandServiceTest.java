package org.hoongoin.interviewbank.interview.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
class InterviewEntityCommandServiceTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private InterviewCommandService interviewCommandService;

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Autowired
	private QuestionRepository questionRepository;

	private final JobCategoryEntity testJobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();

	@Sql(scripts = "classpath:/job-category-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	void insertInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		String title = "title";

		Interview testInterview = InterviewTestFactory.createInterview(testAccountEntity.getId());

		//when
		Interview interview = interviewCommandService.insertInterview(testInterview);

		//then
		assertThat(interview.getTitle()).isEqualTo(title);
		assertThat(interview.getAccountId()).isEqualTo(testAccountEntity.getId());
	}

	@Sql(scripts = "classpath:/job-category-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Test
	void updateInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.saveAndFlush(AccountTestFactory.createAccountEntity());

		String title = "title";
		String newTitle = "newTitle";

		jobCategoryRepository.saveAndFlush(testJobCategoryEntity);

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(testAccountEntity,
			testJobCategoryEntity);
		InterviewEntity savedInterviewEntity = interviewRepository.saveAndFlush(interviewEntity);

		QuestionEntity question1 = InterviewTestFactory.createQuestionEntity("content1", savedInterviewEntity);
		QuestionEntity question2 = InterviewTestFactory.createQuestionEntity("content2", savedInterviewEntity);

		List<QuestionEntity> questions = new ArrayList<>();

		questions.add(question1);
		questions.add(question2);

		questionRepository.saveAllAndFlush(questions);

		List<QuestionEntity> questionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			savedInterviewEntity);

		UpdateInterviewRequest.Question updatedQuestion1 = new UpdateInterviewRequest.Question("newContent1",
			questionEntities.get(0).getId());
		UpdateInterviewRequest.Question updatedQuestion2 = new UpdateInterviewRequest.Question("newContent2",
			questionEntities.get(1).getId());

		List<UpdateInterviewRequest.Question> updatedQuestions = new ArrayList<>();

		updatedQuestions.add(updatedQuestion1);
		updatedQuestions.add(updatedQuestion2);

		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(updatedQuestions, newTitle, null,
			InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR);

		//when
		Interview updatedInterview = interviewCommandService.updateInterview(
			Interview.builder()
				.title(updateInterviewRequest.getTitle())
				.jobCategoryId(1L)
				.build(),
			savedInterviewEntity.getId(),
			testAccountEntity.getId());

		//then
		assertThat(updatedInterview.getTitle()).isEqualTo(newTitle);
	}

	@Test
	void updateInterview_Fail_AccountNotMatch() {
		//given
		AccountEntity testAccountEntity = accountRepository.saveAndFlush(AccountTestFactory.createAccountEntity());

		String title = "title";
		String newTitle = "newTitle";

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(title, testAccountEntity.getId());
		InterviewEntity interviewEntity = InterviewEntity.builder()
			.title(createInterviewRequest.getTitle())
			.accountEntity(testAccountEntity)
			.build();
		InterviewEntity savedInterviewEntity = interviewRepository.saveAndFlush(interviewEntity);

		QuestionEntity question1 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content("content1")
			.build();
		QuestionEntity question2 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content("content2")
			.build();

		List<QuestionEntity> questions = new ArrayList<>();

		questions.add(question1);
		questions.add(question2);

		questionRepository.saveAllAndFlush(questions);

		List<QuestionEntity> questionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			savedInterviewEntity);

		UpdateInterviewRequest.Question updatedQuestion1 = new UpdateInterviewRequest.Question("newContent1",
			questionEntities.get(0).getId());
		UpdateInterviewRequest.Question updatedQuestion2 = new UpdateInterviewRequest.Question("newContent2",
			questionEntities.get(1).getId());

		List<UpdateInterviewRequest.Question> updatedQuestions = new ArrayList<>();

		updatedQuestions.add(updatedQuestion1);
		updatedQuestions.add(updatedQuestion2);

		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(updatedQuestions, newTitle, null,
			InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR);

		Interview testInterview = Interview.builder().title(updateInterviewRequest.getTitle()).build();

		//when //then
		assertThatThrownBy(() -> interviewCommandService.updateInterview(testInterview, savedInterviewEntity.getId(),
			testAccountEntity.getId() + 1))
			.isInstanceOf(IbAccountNotMatchException.class)
			.hasMessageContaining("Bad Request");

	}

}
