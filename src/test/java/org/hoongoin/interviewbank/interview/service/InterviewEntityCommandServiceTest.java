package org.hoongoin.interviewbank.interview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void insertInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(title, testAccountEntity.getId());

		//when
		long createdInterviewId = interviewCommandService.insertInterview(createInterviewRequest);
		Optional<InterviewEntity> interview = interviewRepository.findById(createdInterviewId);

		//then
		assertThat(interview).isPresent();
		assertThat(interview.get().getTitle()).isEqualTo(title);
		assertThat(interview.get().getAccountEntity().getId()).isEqualTo(testAccountEntity.getId());
	}

	@Test
	void updateInterview_Success() {
		//given
		AccountEntity testAccountEntity = accountRepository.save(createTestAccountEntity());

		String title = "title";
		String newTitle = "newTitle";

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(title, testAccountEntity.getId());

		long createdInterviewId = interviewCommandService.insertInterview(createInterviewRequest);
		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(newTitle);

		//when
		Interview updatedInterview = interviewCommandService.updateInterview(updateInterviewRequest, createdInterviewId);

		//then
		assertThat(updatedInterview.getTitle()).isEqualTo(newTitle);
	}

	private AccountEntity createTestAccountEntity() {
		return AccountEntity.builder().id(testAccountId).password(testPassword).email(testEmail).nickname(testNickname).build();
	}
}