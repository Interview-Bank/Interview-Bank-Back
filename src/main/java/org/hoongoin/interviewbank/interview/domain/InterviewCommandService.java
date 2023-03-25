package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

	private final InterviewRepository interviewRepository;
	private final AccountRepository accountRepository;
	private final JobCategoryRepository jobCategoryRepository;
	private final InterviewMapper interviewMapper;
	private final AccountMapper accountMapper;

	public Interview insertInterview(Interview interview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(interview.getAccountId())
			.orElseThrow(() -> new IbEntityNotFoundException("AccountEntity"));

		JobCategoryEntity jobCategoryEntity = findJobCategoryEntityByJobCategory(interview.getPrimaryJobCategory(),
			interview.getSecondaryJobCategory());
		InterviewEntity interviewEntity = InterviewEntity.builder()
			.title(interview.getTitle())
			.accountEntity(selectedAccountEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.build();

		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		return interviewMapper.interviewEntityToInterview(savedInterviewEntity,
			accountMapper.accountEntityToAccount(selectedAccountEntity));
	}

	public long deleteInterview(long interviewId, long accountId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("InterviewEntity"));

		isMatchInterviewAndAccount(accountId, interviewEntity);

		interviewEntity.deleteEntityByFlag();
		return interviewId;
	}

	public Interview updateInterview(Interview interview, long interviewId, long accountId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("InterviewEntity"));

		isMatchInterviewAndAccount(accountId, interviewEntity);

		JobCategoryEntity jobCategoryEntity = findJobCategoryEntityByJobCategory(interview.getPrimaryJobCategory(),
			interview.getSecondaryJobCategory());
		interviewEntity.modifyEntity(interview.getTitle());
		interviewEntity.setJobCategoryEntity(jobCategoryEntity);

		return interviewMapper.interviewEntityToInterview(interviewEntity,
			accountMapper.accountEntityToAccount(interviewEntity.getAccountEntity()));
	}

	private JobCategoryEntity findJobCategoryEntityByJobCategory(String primaryCategory, String secondaryCategory) {
		JobCategoryEntity jobCategoryEntity;
		if (primaryCategory == null){
			jobCategoryEntity = null;
		}
		else if (secondaryCategory == null) {
			jobCategoryEntity = jobCategoryRepository.findByName(primaryCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
		}
		else{
			jobCategoryEntity = jobCategoryRepository.findByNameAndParentJobCategoryName(
					secondaryCategory, primaryCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
		}
		return jobCategoryEntity;
	}

	private void isMatchInterviewAndAccount(long accountId, InterviewEntity interviewEntity) {
		if (interviewEntity.getAccountEntity().getId() != accountId) {
			throw new IbAccountNotMatchException("Interview");
		}
	}
}
