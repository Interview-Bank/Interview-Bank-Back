package org.hoongoin.interviewbank.interview.controller;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

@IbSpringBootTest
@Transactional
class InterviewControllerTest {

	@Autowired
	private InterviewController interviewController;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private InterviewRepository interviewRepository;

	private static final String testTitle = "title";
	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void createInterview_Success() {
		//given
		AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
			.nickname(testNickname)
			.password(testPassword)
			.email(testEmail)
			.build());

		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(testTitle, savedAccount.getId());

		//when
		ResponseEntity<Long> createdInterviewId = interviewController.createInterview(createInterviewRequest);

		//then
		assertThat(createdInterviewId.getBody()).isEqualTo(interviewRepository.findById(createdInterviewId.getBody()).get().getId());
	}

	@Test
	void updateInterview_Success() {
		//given
		AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
			.nickname(testNickname)
			.password(testPassword)
			.email(testEmail)
			.build());
		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(testTitle, savedAccount.getId());
		ResponseEntity<Long> createdInterviewId = interviewController.createInterview(createInterviewRequest);
		String updatedTitle = "updateTitle";
		UpdateInterviewRequest updateInterviewRequest = new UpdateInterviewRequest(updatedTitle);

		//when
		ResponseEntity<UpdateInterviewResponse> updateInterviewResponseResponse = interviewController.updateInterview(
			updateInterviewRequest, createdInterviewId.getBody());

		//then
		assertThat(updateInterviewResponseResponse.getBody().getTitle()).isEqualTo(updatedTitle);
	}

	@Test
	void deleteInterview_Success() {
		//given
		AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
			.nickname(testNickname)
			.password(testPassword)
			.email(testEmail)
			.build());
		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(testTitle, savedAccount.getId());
		ResponseEntity<Long> createdInterviewId = interviewController.createInterview(createInterviewRequest);

		//when
		ResponseEntity<Long> deletedInterviewId = interviewController.deleteInterview(createdInterviewId.getBody());

		//then
		assertThat(interviewRepository.findById(deletedInterviewId.getBody())).isEmpty();
	}

	@Test
	void findInterview_Success() {
		//given
		AccountEntity savedAccount = accountRepository.save(AccountEntity.builder()
			.nickname(testNickname)
			.password(testPassword)
			.email(testEmail)
			.build());
		CreateInterviewRequest createInterviewRequest = new CreateInterviewRequest(testTitle, savedAccount.getId());
		ResponseEntity<Long> createdInterviewId = interviewController.createInterview(createInterviewRequest);

		//when
		ResponseEntity<FindInterviewResponse> findInterviewResponseResponse = interviewController.findInterview(
			createdInterviewId.getBody());

		//then
		assertThat(findInterviewResponseResponse.getBody().getInterviewId()).isEqualTo(createdInterviewId.getBody());
		assertThat(findInterviewResponseResponse.getBody().getTitle()).isEqualTo(createInterviewRequest.getTitle());
	}
}