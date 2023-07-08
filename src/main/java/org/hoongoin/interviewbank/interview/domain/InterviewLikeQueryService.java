package org.hoongoin.interviewbank.interview.domain;

import java.util.Optional;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewLikeEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewLikeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InterviewLikeQueryService {

	private final InterviewLikeRepository interviewLikeRepository;

	public Optional<InterviewLikeEntity> findByAccountIdAndInterviewId(long accountId, long interviewId){
		return interviewLikeRepository.findByAccountIdAndInterviewId(accountId, interviewId);
	}
}
