package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewQueryService {

	private final InterviewRepository interviewRepository;
	private final InterviewMapper interviewMapper;
	private final AccountMapper accountMapper;

	public Interview findEntityById(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("Interview"));

		return interviewMapper.interviewEntityToInterview(interviewEntity,
			accountMapper.accountEntityToAccount(interviewEntity.getAccountEntity()));
	}
}
