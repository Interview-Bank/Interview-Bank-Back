package org.hoongoin.interviewbank.interview.infrastructure.entity;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class InterviewEntityTest {

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private AccountRepository accountRepository;

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	@Test
	void interviewEntity_Success_TitleLengthIs128() {
		//given
		String title = "a".repeat(128);

		AccountEntity savedAccountEntity = accountRepository.saveAndFlush(createTestAccountEntity());

		//when
		InterviewEntity interviewEntity = InterviewEntity.builder().title(title).accountEntity(savedAccountEntity).build();
		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		//then
		assertThat(savedInterviewEntity.getTitle()).isEqualTo(interviewEntity.getTitle());

	}

	@Test
	void interviewEntity_Fail_TitleLengthIs129() {
		//given
		String title = "a".repeat(129);

		AccountEntity savedAccountEntity = accountRepository.saveAndFlush(createTestAccountEntity());

		InterviewEntity interviewEntity = InterviewEntity.builder().title(title).accountEntity(savedAccountEntity).build();

		//when //then
		assertThatThrownBy(() -> interviewRepository.save(interviewEntity)).isInstanceOf(
			DataIntegrityViolationException.class);
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