package org.hoongoin.interviewbank.interview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.hoongoin.interviewbank.interview.service.domain.Question;

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
	private AccountMapper accountMapper;

	@Autowired
	private QuestionRepository questionRepository;

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void insertInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		Interview testInterview = new Interview(title, testAccountEntity.getId());

		//when
		Long createdInterviewId = interviewCommandService.insertInterview(testInterview);
		Optional<InterviewEntity> interview = interviewRepository.findById(createdInterviewId);

		//then
		assertThat(interview).isPresent();
		assertThat(interview.get().getTitle()).isEqualTo(title);
		assertThat(interview.get().getAccountEntity().getId()).isEqualTo(testAccountEntity.getId());
	}

	@Test
	void updateInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.saveAndFlush(createTestAccountEntity());

		String title = "title";
		String newTitle = "newTitle";

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(title, testAccountEntity.getId());
		InterviewEntity interviewEntity = InterviewEntity.builder().title(createInterviewRequest.getTitle()).accountEntity(testAccountEntity).build();
		InterviewEntity savedInterviewEntity = interviewRepository.saveAndFlush(interviewEntity);

		QuestionEntity question1 = QuestionEntity.builder().interviewEntity(savedInterviewEntity).content("content1").build();
		QuestionEntity question2 = QuestionEntity.builder().interviewEntity(savedInterviewEntity).content("content2").build();

		List<QuestionEntity> questions = new ArrayList<>();

		questions.add(question1);
		questions.add(question2);

		questionRepository.saveAllAndFlush(questions);

		List<QuestionEntity> questionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			savedInterviewEntity);

		UpdateInterviewRequest.Question updatedQuestion1 = new UpdateInterviewRequest.Question("newContent1", questionEntities.get(0).getId());
		UpdateInterviewRequest.Question updatedQuestion2 = new UpdateInterviewRequest.Question("newContent2", questionEntities.get(1).getId());

		List<UpdateInterviewRequest.Question> updatedQuestions = new ArrayList<>();

		updatedQuestions.add(updatedQuestion1);
		updatedQuestions.add(updatedQuestion2);


		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(updatedQuestions, newTitle);

		//when
		Interview updatedInterview = interviewCommandService.updateInterview(updateInterviewRequest,
			savedInterviewEntity.getId());

		//then
		assertThat(updatedInterview.getTitle()).isEqualTo(newTitle);
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