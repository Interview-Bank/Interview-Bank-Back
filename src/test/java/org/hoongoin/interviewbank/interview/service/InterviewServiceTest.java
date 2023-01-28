package org.hoongoin.interviewbank.interview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
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
			title, testAccountEntity.getId(), questionsRequest);

		//when
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest);

		//then
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).extracting("content").containsExactlyInAnyOrder("content1",
			"content2");
		assertThat(createInterviewAndQuestionsResponse.getTitle()).isEqualTo(title);
		assertThat(createInterviewAndQuestionsResponse.getQuestions()).hasSize(questionsRequest.getQuestions().size());
		assertThat(createInterviewAndQuestionsResponse.getInterviewId()).isNotNull();

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
