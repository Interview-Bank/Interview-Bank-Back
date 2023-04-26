package org.hoongoin.interviewbank.interview.infrastructure.entity;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class InterviewEntityTest {

	@Autowired
	private InterviewRepository interviewRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Test
	void interviewEntity_Success_TitleLengthIs128() {
		//given
		String title = "a".repeat(128);

		JobCategoryEntity jobCategoryEntity = jobCategoryRepository.saveAndFlush(
			InterviewTestFactory.createJobCategoryEntity());

		AccountEntity savedAccountEntity = accountRepository.saveAndFlush(AccountTestFactory.createAccountEntity());

		//when
		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(savedAccountEntity,
			jobCategoryEntity);
		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		//then
		assertThat(savedInterviewEntity.getTitle()).isEqualTo(interviewEntity.getTitle());

	}

	@Test
	void interviewEntity_Fail_TitleLengthIs129() {
		//given
		String title = "a".repeat(129);

		JobCategoryEntity jobCategoryEntity = jobCategoryRepository.saveAndFlush(InterviewTestFactory.createJobCategoryEntity());

		AccountEntity savedAccountEntity = accountRepository.saveAndFlush(AccountTestFactory.createAccountEntity());

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(savedAccountEntity, jobCategoryEntity);

		//when //then
		assertThatThrownBy(() -> interviewRepository.save(interviewEntity)).isInstanceOf(
			DataIntegrityViolationException.class);
	}
}
