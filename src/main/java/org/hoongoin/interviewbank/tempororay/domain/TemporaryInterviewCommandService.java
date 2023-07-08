package org.hoongoin.interviewbank.tempororay.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.domain.JobCategoryQueryService;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryInterviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryInterviewCommandService {
	private final TemporaryInterviewRepository temporaryInterviewRepository;
	private final JobCategoryQueryService jobCategoryQueryService;
	private final AccountRepository accountRepository;

	public Long insertTemporaryInterview(TemporaryInterview temporaryInterview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(temporaryInterview.getAccountId())
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		JobCategoryEntity jobCategoryEntity;
		try {
			jobCategoryEntity = jobCategoryQueryService.findJobCategoryEntityById(
				temporaryInterview.getJobCategoryId());
		} catch (IbEntityNotFoundException e) {
			jobCategoryEntity = null;
		}

		TemporaryInterviewEntity temporaryInterviewEntity = TemporaryInterviewEntity.builder()
			.title(temporaryInterview.getTitle())
			.accountEntity(selectedAccountEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.interviewPeriod(temporaryInterview.getInterviewPeriod())
			.careerYear(temporaryInterview.getCareerYear())
			.build();
		TemporaryInterviewEntity savedTemporaryInterviewEntity = temporaryInterviewRepository.save(
			temporaryInterviewEntity);

		return savedTemporaryInterviewEntity.getId();
	}

	public void deleteTemporaryInterview(long id) {
		temporaryInterviewRepository.delete(temporaryInterviewRepository.findById(id).orElseThrow(() -> {
			log.info("Temporary Not Found");
			return new IbEntityNotFoundException("Temporary Not Found");
		}));
	}
}
