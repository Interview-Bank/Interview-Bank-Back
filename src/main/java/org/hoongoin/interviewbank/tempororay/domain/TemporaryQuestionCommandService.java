package org.hoongoin.interviewbank.tempororay.domain;

import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.tempororay.TemporaryMapper;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryInterviewRepository;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryQuestionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryQuestionCommandService {

	private final TemporaryInterviewRepository temporaryInterviewRepository;
	private final TemporaryQuestionRepository temporaryQuestionRepository;

	private final TemporaryMapper temporaryMapper;

	public List<Long> insertTemporaryQuestions(List<TemporaryQuestion> temporaryQuestions,
		Long createdTemporaryInterviewId) {
		TemporaryInterviewEntity temporaryInterviewEntity = temporaryInterviewRepository.findById(
			createdTemporaryInterviewId).orElseThrow(() -> {
			log.info("TemporaryInterview Not Found");
			return new IbEntityNotFoundException("TemporaryInterview Not Found");
		});

		List<TemporaryQuestionEntity> temporaryQuestionEntities = temporaryMapper.temporaryQuestionsToTemporaryQuestionEntities(
			temporaryQuestions, temporaryInterviewEntity);

		List<TemporaryQuestionEntity> savedTemporaryQuestionEntities = temporaryQuestionRepository.saveAll(
			temporaryQuestionEntities);
		return temporaryMapper.temporaryQuestionEntitiesToIds(savedTemporaryQuestionEntities);
	}

	public void deleteTemporaryQuestionsByTemporaryInterviewId(long temporaryInterviewId) {
		temporaryQuestionRepository.deleteTemporaryQuestionEntitiesByTemporaryInterviewId(temporaryInterviewId);
	}
}
