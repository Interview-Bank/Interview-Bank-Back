package org.hoongoin.interviewbank.interview.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.hoongoin.interviewbank.account.AccountTestFactory;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.interview.InterviewTestFactory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
class InterviewRepositoryTest {

	@Autowired
	private InterviewRepository interviewRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private JobCategoryRepository jobCategoryRepository;


	@Transactional
	@Test
	void findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc_Success_jobCategoryOfInterview(){
		//given
		AccountEntity accountEntity = AccountTestFactory.createAccountEntity();
		accountEntity = accountRepository.save(accountEntity);

		JobCategoryEntity jobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();
		jobCategoryEntity = jobCategoryRepository.save(jobCategoryEntity);

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(accountEntity, jobCategoryEntity);
		interviewRepository.save(interviewEntity);

		//when
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
			null, List.of(jobCategoryEntity.getId()), null, null, null,
			null, PageRequest.of(0, 10));

		//then
		assertThat(interviewEntityPage.getTotalElements()).isEqualTo(1);
	}

	@Transactional
	@Test
	void findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc_Success_jobCategoryOfRandom(){
		//given
		AccountEntity accountEntity = AccountTestFactory.createAccountEntity();
		accountEntity = accountRepository.save(accountEntity);

		JobCategoryEntity jobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();
		jobCategoryEntity = jobCategoryRepository.save(jobCategoryEntity);

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(accountEntity, jobCategoryEntity);
		interviewRepository.save(interviewEntity);
		//when
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
			null, List.of(jobCategoryEntity.getId() + 1), null, null, null,
			null, PageRequest.of(0, 10));

		//then
		assertThat(interviewEntityPage.getTotalElements()).isZero();
	}

	@Transactional
	@Test
	void findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc_Success_AllNull(){
		//given
		AccountEntity accountEntity = AccountTestFactory.createAccountEntity();
		accountEntity = accountRepository.save(accountEntity);

		JobCategoryEntity jobCategoryEntity = InterviewTestFactory.createJobCategoryEntity();
		jobCategoryEntity = jobCategoryRepository.save(jobCategoryEntity);

		InterviewEntity interviewEntity = InterviewTestFactory.createInterviewEntity(accountEntity, jobCategoryEntity);
		interviewRepository.save(interviewEntity);

		//when
		Page<InterviewEntity> interviewEntityPage = interviewRepository.findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
			null, null, null, null, null,
			null, PageRequest.of(0, 10));

		//then
		assertThat(interviewEntityPage.getTotalElements()).isEqualTo(1);
	}

}
