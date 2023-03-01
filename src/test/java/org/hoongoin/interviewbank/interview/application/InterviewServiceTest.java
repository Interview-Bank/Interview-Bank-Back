package org.hoongoin.interviewbank.interview.application;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
class InterviewServiceTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private InterviewService interviewService;

	@Autowired
	private AccountMapper accountMapper;

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void createInterviewAndQuestionsByRequest_Success() {
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
			title, questionsRequest);

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		//then
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).extracting("content")
			.containsExactlyInAnyOrder("content1",
				"content2");
		assertThat(createInterviewAndQuestionsResponse.getTitle()).isEqualTo(title);
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).hasSize(questionsRequest.getQuestions().size());
		assertThat(createInterviewAndQuestionsResponse.getInterviewId()).isNotNull();

	}

	@Test
	void createInterviewAndQuestionsByRequest_Fail_QuestionSizeIs1001() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		List<QuestionsRequest.Question> questions = new ArrayList<>();

		for (int i = 0; i < 1001; i++) {
			questions.add(new QuestionsRequest.Question("content"));
		}

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = new CreateInterviewAndQuestionsRequest(
			title, questionsRequest);

		//when //then
		assertThatThrownBy(
			() -> interviewService.createInterviewAndQuestionsByRequest(createInterviewAndQuestionsRequest,
				testAccountEntity.getId())).hasMessage("Question Size Validation Failed").isInstanceOf(IbValidationException.class);
	}

	@Test
	void createInterviewAndQuestionsByRequest_Success_QuestionSizeIs1000() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		List<QuestionsRequest.Question> questions = new ArrayList<>();

		for (int i = 0; i < 1000; i++) {
			questions.add(new QuestionsRequest.Question("content"));
		}

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = new CreateInterviewAndQuestionsRequest(
			title, questionsRequest);

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		//then
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).hasSize(1000);
	}

	@Test
	void findInterviewPageByPageAndSize_Success_SoftDeleteSize() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		List<QuestionsRequest.Question> questions = new ArrayList<>();

		for (int i = 0; i < 1000; i++) {
			questions.add(new QuestionsRequest.Question("content"));
		}

		QuestionsRequest questionsRequest = new QuestionsRequest(questions);

		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest = new CreateInterviewAndQuestionsRequest(
			title, questionsRequest);

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, testAccountEntity.getId());

		Long interviewId = createInterviewAndQuestionsResponse.getInterviewId();

		interviewService.deleteInterviewById(interviewId, testAccountEntity.getId());

		//when
		FindInterviewPageResponse interviewPageByPageAndSize = interviewService.findInterviewPageByPageAndSize(0, 1);

		//then
		assertThat(interviewPageByPageAndSize.getInterviews()).isEmpty();
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
