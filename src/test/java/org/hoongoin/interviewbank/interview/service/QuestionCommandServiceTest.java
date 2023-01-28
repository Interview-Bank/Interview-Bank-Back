package org.hoongoin.interviewbank.interview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.CreateQuestionRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateQuestionsRequest;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.service.domain.Question;
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
	void insertQuestion_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		InterviewEntity savedInterviewEntity = interviewRepository.save(
			InterviewEntity.builder().accountEntity(testAccountEntity).title(title).build());

		String content = "content1";

		CreateQuestionRequest createQuestionRequest = new CreateQuestionRequest(content, savedInterviewEntity.getId());

		//when
		long insertedQuestion = questionCommandService.insertQuestion(createQuestionRequest);

		//then
		Optional<QuestionEntity> selectedQuestionEntity = questionRepository.findById(insertedQuestion);
		assertThat(selectedQuestionEntity).isPresent();
		assertThat(selectedQuestionEntity.get().getContent()).isEqualTo(content);
		assertThat(selectedQuestionEntity.get().getInterviewEntity().getId()).isEqualTo(savedInterviewEntity.getId());
	}

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
			savedInterviewEntity.getTitle(), testAccountEntity.getId(), questionsRequest);

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
				new UpdateInterviewRequest(updatingQuestions, savedInterviewEntity.getTitle()), savedInterviewEntity.getId()));

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
