package org.hoongoin.interviewbank.tempororay.domain;

import java.util.List;

import org.hoongoin.interviewbank.tempororay.TemporaryMapper;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryQuestionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemporaryQuestionQueryService {

	private final TemporaryQuestionRepository temporaryQuestionRepository;
	private final TemporaryMapper temporaryMapper;

	public List<TemporaryQuestion> findTemporaryQuestionByTemporaryInterviewId(long temporaryInterviewId) {
		List<TemporaryQuestionEntity> temporaryQuestionEntities = temporaryQuestionRepository.findByTemporaryInterviewEntityId(
			temporaryInterviewId);
		return temporaryMapper.temporaryQuestionEntitiesToTemporaryQuestions(temporaryQuestionEntities);
	}
}
