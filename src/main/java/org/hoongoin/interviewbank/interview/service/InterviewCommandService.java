package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;;

import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
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
		interviewRepository.deleteById(interviewId);
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
