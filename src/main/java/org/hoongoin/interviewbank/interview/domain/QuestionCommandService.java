package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.InterviewRepository;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.hoongoin.interviewbank.interview.application.entity.Question;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionCommandService {

	private final QuestionCommandServiceAsync questionCommandServiceAsync;
	private final InterviewRepository interviewRepository;
	private final QuestionRepository questionRepository;
	private final InterviewMapper interviewMapper;

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

	public List<Question> updateQuestions(long interviewId, List<Question> questions) {
		//TODO: separate method

		List<Question> allUpdatedQuestions = new ArrayList<>();

		List<QuestionEntity> originalQuestionEntities = questionRepository.findAllByInterviewId(interviewId);
		Map<Long, QuestionEntity> originalQuestionEntityMap = originalQuestionEntities.stream()
			.collect(Collectors.toMap(QuestionEntity::getId, Function.identity()));

		List<QuestionEntity> newQuestionEntities = new ArrayList<>();
		List<QuestionEntity> modifiedQuestionEntities = new ArrayList<>();

		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> {
				log.info("Interview Not Found");
				return new IbEntityNotFoundException("Interview Not Found");
			});

		questions.forEach(question -> {
			QuestionEntity questionEntity;

			if (question.getQuestionId() != null) {
				questionEntity = originalQuestionEntityMap.remove(question.getQuestionId());
				if (questionEntity == null) {
					log.info("Question Not Found");
					throw new IbEntityNotFoundException("Question Not Found");
				}

				if (this.isContentUpdated(question, questionEntity)) {
					questionEntity.modifyContent(question.getContent());
					modifiedQuestionEntities.add(questionEntity);
				}

			} else {
				questionEntity = QuestionEntity.builder()
					.interviewEntity(interviewEntity)
					.content(question.getContent())
					.gptAnswer("GPT가 답변을 생성중입니다.")
					.build();
				newQuestionEntities.add(questionEntity);
			}

			allUpdatedQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity));
		});

		questionRepository.saveAll(newQuestionEntities);
		originalQuestionEntityMap.values().forEach(SoftDeletedBaseEntity::deleteEntityByFlag);

		modifiedQuestionEntities.addAll(newQuestionEntities);
		questionCommandServiceAsync.updateAllGptAnswer(modifiedQuestionEntities);

		return allUpdatedQuestions;
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

	private List<Question> saveAllQuestions(List<Question> questions, InterviewEntity interviewEntity) {
		List<QuestionEntity> questionEntities = new ArrayList<>();

		for (Question question : questions) {
			questionEntities.add(QuestionEntity.builder()
				.content(question.getContent())
				.gptAnswer("GPT가 답변을 생성중입니다.")
				.interviewEntity(interviewEntity)
				.build());
		}

		List<QuestionEntity> savedQuestionEntities = saveAllQuestionWithBatch(questionEntities);

		questionCommandServiceAsync.updateAllGptAnswer(savedQuestionEntities);

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

	private boolean isContentUpdated(Question question, QuestionEntity questionEntity) {
		return !Objects.equals(question.getContent(), questionEntity.getContent());
	}
}
