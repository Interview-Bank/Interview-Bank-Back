package org.hoongoin.interviewbank.tempororay.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryInterview;
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
	private final AccountRepository accountRepository;

	public Long insertTemporaryInterview(TemporaryInterview temporaryInterview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(temporaryInterview.getAccountId())
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		TemporaryInterviewEntity temporaryInterviewEntity = TemporaryInterviewEntity.builder()
			.title(temporaryInterview.getTitle())
			.accountEntity(selectedAccountEntity)
			.jobCategoryId(temporaryInterview.getJobCategoryId())
			.interviewPeriod(temporaryInterview.getInterviewPeriod())
			.careerYear(temporaryInterview.getCareerYear())
			.build();
		TemporaryInterviewEntity savedTemporaryInterviewEntity = temporaryInterviewRepository.save(
			temporaryInterviewEntity);

		return savedTemporaryInterviewEntity.getId();
	}

	public void deleteTemporaryInterview(long temporaryInterviewId) {
		temporaryInterviewRepository.deleteTemporaryInterviewEntityById(temporaryInterviewId);
	}
}
