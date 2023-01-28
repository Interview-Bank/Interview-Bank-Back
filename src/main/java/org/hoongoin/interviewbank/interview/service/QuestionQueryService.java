package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.interview.InterviewMapper;
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

	public List<Question> findEntitiesByInterviewId(long interviewId) {
		List<QuestionEntity> questionEntities = questionRepository.findAllByInterviewId(interviewId);
		List<Question> questions = new ArrayList<>();
		for(QuestionEntity questionEntity: questionEntities){
			questions.add(interviewMapper.questionEntityToQuestion(questionEntity));
		}
		return questions;
	}
}
