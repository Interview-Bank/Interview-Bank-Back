package org.hoongoin.interviewbank.tempororay.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.hoongoin.interviewbank.tempororay.infrastructure.repository.TemporaryInterviewRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryQuestionCommandService {

	private final TemporaryInterviewRepository temporaryInterviewRepository;

	public List<Long> insertTemporaryQuestions(List<TemporaryQuestion> temporaryQuestions,
		Long createdTemporaryInterviewId) {
		TemporaryInterviewEntity temporaryInterviewEntity = temporaryInterviewRepository.findById(
			createdTemporaryInterviewId).orElseThrow(() -> {
			log.info("TemporaryInterview Not Found");
			return new IbEntityNotFoundException("TemporaryInterview Not Found");
		});

		return saveAllTemporaryQuestions(temporaryQuestions, temporaryInterviewEntity);
	}

	private List<Long> saveAllTemporaryQuestions(List<TemporaryQuestion> temporaryQuestions,
		TemporaryInterviewEntity temporaryInterviewEntity) {
		List<Long> temporaryQuestionEntityIds = new ArrayList<>();

		for (TemporaryQuestion temporaryQuestion : temporaryQuestions) {
			temporaryQuestionEntityIds.add(TemporaryQuestionEntity.builder()
				.content(temporaryQuestion.getContent())
				.temporaryInterviewEntity(temporaryInterviewEntity)
				.build().getId());
		}

		return temporaryQuestionEntityIds;
	}
}
