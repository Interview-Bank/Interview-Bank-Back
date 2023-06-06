package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.TemporaryInterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.TemporaryQuestionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryInterviewCommandService {
	private final TemporaryInterviewRepository temporaryInterviewRepository;
	private final TemporaryQuestionRepository temporaryQuestionRepository;
	private final JobCategoryQueryService jobCategoryQueryService;
	private final AccountRepository accountRepository;
	private final InterviewMapper interviewMapper;


	public Long insertTemporaryInterview(TemporaryInterview temporaryInterview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(temporaryInterview.getAccountId())
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		JobCategoryEntity jobCategoryEntity = jobCategoryQueryService.findJobCategoryEntityById(
			temporaryInterview.getJobCategoryId());
		TemporaryInterviewEntity temporaryInterviewEntity = TemporaryInterviewEntity.builder()
			.title(temporaryInterview.getTitle())
			.accountEntity(selectedAccountEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.interviewPeriod(temporaryInterview.getInterviewPeriod())
			.careerYear(temporaryInterview.getCareerYear())
			.build();
		TemporaryInterviewEntity savedTemporaryInterviewEntity = temporaryInterviewRepository.save(temporaryInterviewEntity);

		return savedTemporaryInterviewEntity.getId();
	}

}
