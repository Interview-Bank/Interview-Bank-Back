package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

	public List<Question> updateQuestions(long interviewId, List<Question> questions) {
		List<Question> newQuestions = new ArrayList<>();
		List<Question> questionsToUpdateGptAnswer = new ArrayList<>();

		List<QuestionEntity> questionEntities = questionRepository.findAllByInterviewId(interviewId);
		InterviewEntity interviewEntity = interviewRepository.findById(interviewId)
			.orElseThrow(() -> {
				log.info("Interview Not Found");
				return new IbEntityNotFoundException("Interview Not Found");
			});

		questions.forEach(question -> {
			QuestionEntity questionEntity;

			if (question.getQuestionId() != null) {
				questionEntity = questionEntities.stream()
					.filter(questionEntityInner -> question.getQuestionId().equals(questionEntityInner.getId()))
					.findFirst().orElseThrow(() -> {
						log.info("Question Not Found");
						return new IbEntityNotFoundException("Question Not Found");
					});

				if (this.isContentUpdated(question, questionEntity)) {
					questionEntity.modifyContent(question.getContent());
					questionsToUpdateGptAnswer.add(interviewMapper.questionEntityToQuestion(questionEntity));
				}

			} else {
				questionEntity = QuestionEntity.builder()
					.interviewEntity(interviewEntity)
					.content(question.getContent())
					.build();
				questionRepository.save(questionEntity);
				questionsToUpdateGptAnswer.add(interviewMapper.questionEntityToQuestion(questionEntity));
			}
			newQuestions.add(interviewMapper.questionEntityToQuestion(questionEntity));
		});

		updateGptAnswersAsync(questionsToUpdateGptAnswer);

		return newQuestions;
	}

	private boolean isContentUpdated(Question question, QuestionEntity questionEntity) {
		return !Objects.equals(question.getContent(), questionEntity.getContent());
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

	@Async
	@Transactional
	public CompletableFuture<Void> updateGptAnswersAsync(List<Question> questions) {
		GptRequestBody.Message assistantMessage = new GptRequestBody.Message(MessageRole.ASSISTANT.getRole(),
			"You are a Interview Q&A Assistant.");
		questions.forEach(question -> {
			GptRequestBody.Message questionMessage = new GptRequestBody.Message(MessageRole.USER.getRole(),
				question.getContent());
			GptResponseBody gptResponseBody = gptRequestHandler.sendChatCompletionRequest(
				List.of(assistantMessage, questionMessage));
			this.udpateGptAnswerOfQuestion(question.getQuestionId(),
				gptResponseBody.getChoices().get(0).getMessage().getContent());
		});

		return CompletableFuture.completedFuture(null);
	}

	public Question udpateGptAnswerOfQuestion(Long questionId, String gptAnswer) {
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
