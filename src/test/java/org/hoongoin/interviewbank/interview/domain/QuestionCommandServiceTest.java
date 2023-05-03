package org.hoongoin.interviewbank.interview.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
class QuestionCommandServiceTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionCommandService questionCommandService;

	@Autowired
	private InterviewMapper interviewMapper;

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	private static final JobCategoryEntity testJobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();

	@Test
	void insertQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		JobCategoryEntity savedTestJobCategoryEntity = jobCategoryRepository.saveAndFlush(testJobCategoryEntity);

		int questionSize = 2;

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewTestFactory.createInterviewEntity(testAccountEntity, savedTestJobCategoryEntity));

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = InterviewTestFactory.createCreateInterviewAndQuestionsRequest(
			questionSize);

		//when
		List<Question> questions = questionCommandService.insertQuestions(
			interviewMapper.createInterviewAndQuestionsRequestToQuestions(createInterviewAndQuestionsRequest,
				savedInterviewEntity.getId()),
			savedInterviewEntity.getId());

		//then
		assertThat(questions).hasSize(questionSize);
		assertThat(questions)
			.extracting(Question::getContent)
			.containsExactlyInAnyOrder("content1", "content2");
	}

	@Test
	void updateQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(AccountTestFactory.createAccountEntity());

		JobCategoryEntity savedTestJobCategoryEntity = jobCategoryRepository.saveAndFlush(testJobCategoryEntity);

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewTestFactory.createInterviewEntity(testAccountEntity, savedTestJobCategoryEntity));

		String content1 = "content1";
		String content2 = "content2";

		QuestionEntity question1 = InterviewTestFactory.createQuestionEntity(content1, savedInterviewEntity);
		QuestionEntity question2 = InterviewTestFactory.createQuestionEntity(content2, savedInterviewEntity);

		List<QuestionEntity> questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);

		List<QuestionEntity> questionEntities = questionRepository.saveAllAndFlush(questions);

		List<UpdateInterviewRequest.Question> updatingQuestions = new ArrayList<>();

		String updatedContent1 = "content11";
		String updatedContent2 = "content22";

		questionEntities.forEach(
			questionEntity -> updatingQuestions.add(
				new UpdateInterviewRequest.Question(questionEntity.getContent(), questionEntity.getId())));

		updatingQuestions.get(0).setContent(updatedContent1);
		updatingQuestions.get(1).setContent(updatedContent2);

		//when
		List<Question> updatedQuestions = questionCommandService.updateQuestions(
			savedInterviewEntity.getId(),
			interviewMapper.updateInterviewRequestToQuestions(
				new UpdateInterviewRequest(updatingQuestions, savedInterviewEntity.getTitle(), null,
					InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR),
				savedInterviewEntity.getId()));

		//then
		assertThat(updatedQuestions.get(0).getContent()).isEqualTo(updatedContent1);
		assertThat(updatedQuestions.get(1).getContent()).isEqualTo(updatedContent2);
	}

	@Test
	void deleteQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.saveAndFlush(AccountTestFactory.createAccountEntity());

		JobCategoryEntity savedTestJobCategoryEntity = jobCategoryRepository.saveAndFlush(testJobCategoryEntity);

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewTestFactory.createInterviewEntity(testAccountEntity, savedTestJobCategoryEntity));

		String content1 = "content1";
		String content2 = "content2";

		QuestionEntity question1 = InterviewTestFactory.createQuestionEntity(content1, savedInterviewEntity);
		QuestionEntity question2 = InterviewTestFactory.createQuestionEntity(content2, savedInterviewEntity);

		List<QuestionEntity> questions = new ArrayList<>();
		questions.add(question1);
		questions.add(question2);

		questionRepository.saveAllAndFlush(questions);

		//when
		List<Long> deletedQuestionIds = questionCommandService.deleteQuestionsByInterviewId(
			savedInterviewEntity.getId());

		//then
		deletedQuestionIds.forEach(deletedQuestionId -> {
			QuestionEntity question = questionRepository.findById(deletedQuestionId).orElse(null);
			assert question != null;
			assertThat(question.getDeletedFlag()).isTrue();
		});

		long deletedQuestionsCount = questions.size();
		long allQuestionsCount = questionRepository.findAll().size();

		assertThat(deletedQuestionsCount).isEqualTo(allQuestionsCount);
	}
}
