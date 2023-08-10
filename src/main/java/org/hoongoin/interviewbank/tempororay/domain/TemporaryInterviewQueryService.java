package org.hoongoin.interviewbank.tempororay.domain;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.tempororay.TemporaryMapper;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryInterviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemporaryInterviewQueryService {

	private final TemporaryInterviewRepository temporaryInterviewRepository;
	private final TemporaryMapper temporaryMapper;

	public TemporaryInterview findTemporaryInterviewById(long temporaryInterviewId) {
		TemporaryInterviewEntity temporaryInterview = temporaryInterviewRepository.findById(temporaryInterviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("TemporaryInterview not found"));
		return temporaryMapper.temporaryInterviewEntityToTemporaryInterview(temporaryInterview);
	}
}
