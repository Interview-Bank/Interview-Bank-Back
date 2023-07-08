package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.application.entity.InterviewLike;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewLikeEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewLikeRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class InterviewLikeCommandService {

	private final InterviewLikeRepository interviewLikeRepository;
	private final InterviewRepository interviewRepository;
	private final AccountRepository accountRepository;
	private final InterviewMapper interviewMapper;

	public InterviewLike insertInterviewLike(InterviewLike interviewLike) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewLike.getInterviewLikeId())
			.orElseThrow(() -> {
				log.info("InterviewEntity Not Found");
				throw new IbEntityNotFoundException("InterviewEntity Not Found");
			});

		AccountEntity accountEntity = accountRepository.findById(interviewLike.getAccountId())
			.orElseThrow(() -> {
				log.info("AccountEntity Not Found");
				throw new IbEntityNotFoundException("AccountEntity Not Found");
			});

		InterviewLikeEntity interviewLikeEntity = InterviewLikeEntity.builder()
			.interviewEntity(interviewEntity)
			.accountEntity(accountEntity)
			.like(interviewLike.getLike())
			.build();
		interviewLikeRepository.save(interviewLikeEntity);

		return interviewMapper.interviewLikeEntityToInterviewLike(interviewLikeEntity);
	}
}
