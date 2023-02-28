package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewCommandService {

	private final InterviewRepository interviewRepository;
	private final AccountRepository accountRepository;
	private final InterviewMapper interviewMapper;
	private final AccountMapper accountMapper;

	public long insertInterview(Interview interview) {
		AccountEntity selectedAccountEntity = accountRepository.findById(interview.getAccountId())
			.orElseThrow(() -> new IbEntityNotFoundException("AccountEntity"));

		InterviewEntity interviewEntity = InterviewEntity.builder()
			.title(interview.getTitle())
			.accountEntity(selectedAccountEntity)
			.build();

		InterviewEntity savedInterviewEntity = interviewRepository.save(interviewEntity);

		return savedInterviewEntity.getId();
	}

	public long deleteInterview(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("InterviewEntity"));

		interviewEntity.deleteEntityByFlag();
		return interviewId;
	}

	public Interview updateInterview(Interview interview, long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("InterviewEntity"));

		interviewEntity.modifyEntity(interview.getTitle());

		return interviewMapper.interviewEntityToInterview(interviewEntity,
			accountMapper.accountEntityToAccount(interviewEntity.getAccountEntity()));
	}
}
