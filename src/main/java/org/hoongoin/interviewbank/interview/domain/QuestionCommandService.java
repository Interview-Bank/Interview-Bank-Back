package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;
import org.hoongoin.interviewbank.common.gpt.GptRequestBody;
import org.hoongoin.interviewbank.common.gpt.GptRequestHandler;
import org.hoongoin.interviewbank.common.gpt.GptResponseBody;
import org.hoongoin.interviewbank.common.gpt.MessageRole;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionCommandService {

	private final InterviewRepository interviewRepository;
	private final QuestionRepository questionRepository;
	private final InterviewMapper interviewMapper;
	private final GptRequestHandler gptRequestHandler;

	@PersistenceContext
	private EntityManager entityManager;

	private static final String GET_BATCH_SIZE = "javax.persistence.jdbc.batch_size";

	public List<Question> insertQuestions(List<Question> questions, long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> {
				log.info("Interview Not Found");
				return new IbEntityNotFoundException("Interview Not Found");
			});

		return saveAllQuestions(questions, interviewEntity);
	}

	public List<Question> updateQuestions(long interviewId, List<Question> newQuestions) {
		List<Question> updatedQuestions = new ArrayList<>();

		List<QuestionEntity> questionEntities = questionRepository.findAllByInterviewId(interviewId);

		if (questionEntities.size() < newQuestions.size()) {
			for (int i = 0; i < questionEntities.size(); i++) {
				QuestionEntity questionEntity = questionEntities.get(i);
				Question question = newQuestions.get(i);

				questionEntity.modifyContent(question.getContent());

				updatedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity));
			}

			InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
				.orElseThrow(() -> new IbEntityNotFoundException("interview"));

			List<QuestionEntity> newQuestionEntities = new ArrayList<>();

			for (Question question : newQuestions.subList(questionEntities.size(), newQuestions.size())) {
				newQuestionEntities.add(QuestionEntity.builder()
					.content(question.getContent())
					.interviewEntity(interviewEntity)
					.build());
			}

			List<QuestionEntity> savedQuestionEntities = saveAllQuestionWithBatch(newQuestionEntities);
			savedQuestionEntities.forEach(
				questionEntity -> updatedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity)));

		} else if (questionEntities.size() == newQuestions.size()) {
			questionEntities.forEach(questionEntity -> {
				Question question = newQuestions.get(questionEntities.indexOf(questionEntity));

				questionEntity.modifyContent(question.getContent());

				updatedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity));
			});

		} else {
			for (int i = 0; i < newQuestions.size(); i++) {
				QuestionEntity questionEntity = questionEntities.get(i);
				Question question = newQuestions.get(i);

				questionEntity.modifyContent(question.getContent());

				updatedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity));
			}

			List<QuestionEntity> deletedQuestionEntities = questionEntities.subList(newQuestions.size(), questionEntities.size());
			deletedQuestionEntities.forEach(SoftDeletedBaseEntity::deleteEntityByFlag);
		}

		return updatedQuestions;
	}

	public List<Long> deleteQuestionsByInterviewId(long interviewId) {
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> {
				log.info("Interview Not Found");
				return new IbEntityNotFoundException("Interview Not Found");
			});

		List<Long> deletedQuestions = new ArrayList<>();

		List<QuestionEntity> registeredQuestionEntities = questionRepository.findQuestionEntitiesByInterviewEntity(
			interviewEntity);

		registeredQuestionEntities.forEach(SoftDeletedBaseEntity::deleteEntityByFlag);

		registeredQuestionEntities.forEach(
			questionEntity -> deletedQuestions.add(questionEntity.getId()));

		return deletedQuestions;
	}

	public Question udpateGptAnswerOfQuestion(Long questionId, String gptAnswer){
		QuestionEntity questionEntity = questionRepository.findById(questionId)
			.orElseThrow(() -> {
				log.info("Question Not Found");
				return new IbEntityNotFoundException("Question Not Found");
			});

		questionEntity.modifyGptAnswer(gptAnswer);

		return interviewMapper.questionEntityToQuestion(questionEntity);
	}

	private List<Question> saveAllQuestions(List<Question> questions, InterviewEntity interviewEntity) {
		List<QuestionEntity> questionEntities = new ArrayList<>();

		for (Question question : questions) {
			questionEntities.add(QuestionEntity.builder()
				.content(question.getContent())
				.interviewEntity(interviewEntity)
				.build());
		}

		List<QuestionEntity> savedQuestionEntities = saveAllQuestionWithBatch(questionEntities);

		List<Question> returnQuestions = new ArrayList<>();

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
