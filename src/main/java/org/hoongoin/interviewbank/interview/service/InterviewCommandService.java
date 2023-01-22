package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.exception.CustomEntityNotFoundException;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

	private final InterviewRepository interviewRepository;
	private final AccountRepository accountRepository;
	private final InterviewMapper interviewMapper;
	private final AccountMapper accountMapper;

	@Transactional
	public long insertInterview(CreateInterviewRequest createInterviewRequest) {
		AccountEntity selectedAccountEntity = accountRepository.findById(createInterviewRequest.getAccountId())
			.orElseThrow(() -> new CustomEntityNotFoundException("AccountEntity"));

		InterviewEntity interviewEntity = interviewMapper.createInterviewRequestToInterviewEntity(createInterviewRequest,
			selectedAccountEntity);

		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		return savedInterviewEntity.getId();
	}

	@Transactional
	public long deleteInterview(long interviewId) {
		interviewRepository.deleteById(interviewId);
		return interviewId;
	}

	@Transactional
	public Interview updateInterview(UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new CustomEntityNotFoundException("InterviewEntity"));

		interviewEntity.modifyEntity(updateInterviewRequest.getTitle());

		return interviewMapper.interviewEntityToInterview(interviewEntity, accountMapper.accountEntityToAccount(interviewEntity.getAccountEntity()));
	}
}
