package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;;

import org.hoongoin.interviewbank.interview.application.dto.InterviewModifyDto;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

	private final InterviewRepository interviewRepository;
	private final AccountRepository accountRepository;
	private final JobCategoryQueryService jobCategoryQueryService;
	private final InterviewMapper interviewMapper;

	public Interview insertInterview(Interview interview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(interview.getAccountId())
			.orElseThrow(() -> new IbEntityNotFoundException("AccountEntity"));

		InterviewEntity interviewEntity;
		if(interview.getJobCategoryId() != null){
			JobCategoryEntity jobCategoryEntity = jobCategoryQueryService.findJobCategoryEntityById(
				interview.getJobCategoryId());
			interviewEntity = InterviewEntity.builder()
				.title(interview.getTitle())
				.accountEntity(selectedAccountEntity)
				.jobCategoryEntity(jobCategoryEntity)
				.interviewPeriod(interview.getInterviewPeriod())
				.careerYear(interview.getCareerYear())
				.build();
		}
		else{
			interviewEntity = InterviewEntity.builder()
				.title(interview.getTitle())
				.accountEntity(selectedAccountEntity)
				.interviewPeriod(interview.getInterviewPeriod())
				.careerYear(interview.getCareerYear())
				.build();
		}

		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		return interviewMapper.interviewEntityToInterview(savedInterviewEntity, selectedAccountEntity.getId());
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

		JobCategoryEntity jobCategoryEntity = null;
		if(interview.getJobCategoryId() != null) {
			jobCategoryEntity = jobCategoryQueryService.findJobCategoryEntityById(
				interview.getJobCategoryId());
		}
		interviewEntity.modifyEntity(
			new InterviewModifyDto(interview.getTitle(), interview.getInterviewPeriod(), interview.getCareerYear(),
		  		jobCategoryEntity));

		return interviewMapper.interviewEntityToInterview(interviewEntity, interviewEntity.getAccountEntity().getId());
	}

	private void isMatchInterviewAndAccount(long accountId, InterviewEntity interviewEntity) {
		if (interviewEntity.getAccountEntity().getId() != accountId) {
			throw new IbAccountNotMatchException("Interview");
		}
	}
}
