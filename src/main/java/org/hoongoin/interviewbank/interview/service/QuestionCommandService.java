package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateQuestionRequest;
import org.hoongoin.interviewbank.interview.controller.request.QuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateQuestionsRequest;
import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionCommandService {

	private final InterviewRepository interviewRepository;
	private final QuestionRepository questionRepository;
	private final InterviewMapper interviewMapper;

	@PersistenceContext
	private EntityManager entityManager;

	private static final String GET_BATCH_SIZE = "javax.persistence.jdbc.batch_size";

	public long insertQuestion(CreateQuestionRequest createQuestionRequest) {
		InterviewEntity interviewEntity = interviewRepository.findById(createQuestionRequest.getInterviewId())
			.orElseThrow(() -> new IbEntityNotFoundException("interview"));

		QuestionEntity questionEntity = QuestionEntity.builder()
			.content(createQuestionRequest.getContent())
			.interviewEntity(interviewEntity)
			.build();

		QuestionEntity savedEntity = questionRepository.save(questionEntity);
		return savedEntity.getId();
	}

	public List<Question> insertQuestions(QuestionsRequest questionsRequest, long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("interview"));

		List<QuestionEntity> questionEntities = new ArrayList<>();

		List<Question> questions = new ArrayList<>();

		questionsRequest.getQuestions()
			.forEach(question -> questions.add(
				new Question(interviewId, question.getContent())));

		return saveAllQuestions(questions, interviewEntity, questionEntities);
	}

	public List<Question> updateQuestions(UpdateQuestionsRequest updateQuestionsRequest, long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("interview"));

		List<Question> questions = new ArrayList<>();

		updateQuestionsRequest.getQuestions()
			.forEach(question -> {
				QuestionEntity questionEntity = questionRepository.findById(question.getQuestionId())
					.orElseThrow(() -> new IbEntityNotFoundException("question"));

				questionEntity.modifyContent(question.getContent());

				questions.add(interviewMapper.questionEntityToQuestion(questionEntity));
			});

		return questions;
	}

	public List<Question> deleteQuestionsByInterviewId(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IbEntityNotFoundException("interview"));

		List<Question> deletedQuestions = new ArrayList<>();

		List<QuestionEntity> registeredQuestionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			interviewEntity);

		registeredQuestionEntities.forEach(SoftDeletedBaseEntity::deleteEntityByFlag);

		registeredQuestionEntities.forEach(
			questionEntity -> deletedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity)));

		return deletedQuestions;
	}

	public Question deleteQuestion(long questionId) {
		QuestionEntity questionEntity = questionRepository.findById(questionId)
			.orElseThrow(() -> new IbEntityNotFoundException("question"));

		questionEntity.deleteEntityByFlag();

		return interviewMapper.questionEntityToQuestion(questionEntity);
	}

	private List<Question> saveAllQuestions(List<Question> questions, InterviewEntity interviewEntity,
		List<QuestionEntity> questionEntities) {
		for (Question question : questions) {
			questionEntities.add(QuestionEntity.builder()
				.content(question.getContent())
				.interviewEntity(interviewEntity)
				.build());
		}

		List<Question> returnQuestions = new ArrayList<>();

		List<QuestionEntity> savedQuestionEntities = saveAllQuestionWithBatch(questionEntities);

		savedQuestionEntities.forEach(
			questionEntity -> returnQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity)));

		return returnQuestions;
	}

	private List<QuestionEntity> saveAllQuestionWithBatch(List<QuestionEntity> entities) {
		if (entityManager == null) {
			throw new IllegalArgumentException("The entityManager is not initialized");
		}

		int originalBatchSize = (int)entityManager.getProperties().getOrDefault(GET_BATCH_SIZE, 1);
		entityManager.setProperty(GET_BATCH_SIZE, 50);
		entityManager.clear();
		List<QuestionEntity> savedEntities = questionRepository.saveAll(entities);
		entityManager.flush();
		entityManager.setProperty(GET_BATCH_SIZE, originalBatchSize);

		return savedEntities;
	}
}
