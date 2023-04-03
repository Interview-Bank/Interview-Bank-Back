package org.hoongoin.interviewbank.interview.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
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

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void insertQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewEntity.builder().accountEntity(testAccountEntity).title(title).build());

		String content1 = "content1";
		String content2 = "content2";

		QuestionsRequest.Question innerQuestion1 = new QuestionsRequest.Question(content1);
		QuestionsRequest.Question innerQuestion2 = new QuestionsRequest.Question(content2);

		List<QuestionsRequest.Question> innerQuestions = new ArrayList<>();

		innerQuestions.add(innerQuestion1);
		innerQuestions.add(innerQuestion2);

		QuestionsRequest questionsRequest = new QuestionsRequest(innerQuestions);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = new CreateInterviewAndQuestionsRequest(
			savedInterviewEntity.getTitle(), null, null, InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR,
			questionsRequest);

		//when
		List<Question> questions = questionCommandService.insertQuestions(
			interviewMapper.createInterviewAndQuestionsRequestToQuestions(createInterviewAndQuestionsRequest,
				savedInterviewEntity.getId()),
			savedInterviewEntity.getId());

		//then
		assertThat(questions).hasSize(innerQuestions.size());
		assertThat(questions)
			.extracting(Question::getContent)
			.containsExactlyInAnyOrder(content1, content2);
	}

	@Test
	void updateQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewEntity.builder().accountEntity(testAccountEntity).title(title).build());

		String content1 = "content1";
		String content2 = "content2";

		QuestionEntity question1 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content(content1)
			.build();
		QuestionEntity question2 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content(content2)
			.build();

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
			interviewMapper.updateInterviewRequestToQuestions(
				new UpdateInterviewRequest(updatingQuestions, savedInterviewEntity.getTitle(), null, null,
					InterviewPeriod.EXPECTED_INTERVIEW, CareerYear.FOUR_YEAR),
				savedInterviewEntity.getId()));

		//then
		assertThat(updatedQuestions.get(0).getContent()).isEqualTo(updatedContent1);
		assertThat(updatedQuestions.get(1).getContent()).isEqualTo(updatedContent2);
	}

	@Test
	void deleteQuestions_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewEntity.builder().accountEntity(testAccountEntity).title(title).build());

		String content1 = "content1";
		String content2 = "content2";

		QuestionEntity question1 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content(content1)
			.build();
		QuestionEntity question2 = QuestionEntity.builder()
			.interviewEntity(savedInterviewEntity)
			.content(content2)
			.build();

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

	private AccountEntity createTestAccountEntity() {
		return AccountEntity.builder()
			.id(testAccountId)
			.password(testPassword)
			.email(testEmail)
			.nickname(testNickname)
			.build();
	}
}
