package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.hoongoin.interviewbank.interview.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.interview.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.interview.infrastructure.entity.TemporaryInterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.TemporaryQuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.TemporaryInterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.TemporaryQuestionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TemporaryQuestionCommandService {

	private final TemporaryQuestionRepository temporaryQuestionRepository;
	private final TemporaryInterviewRepository temporaryInterviewRepository;
	private final InterviewMapper interviewMapper;

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
