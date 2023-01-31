package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionQueryService {
	private final InterviewRepository interviewRepository;

	private final QuestionRepository questionRepository;
	private final InterviewMapper interviewMapper;

	public List<Question> findQuestionsByInterviewId(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("interview"));

		List<QuestionEntity> questionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			interviewEntity);

		List<Question> questions = new ArrayList<>();

		questionEntities.forEach(
			questionEntity -> questions.add(interviewMapper.questionEntityToQuestion(questionEntity)));

		return questions;
	}
}
