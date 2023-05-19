package org.hoongoin.interviewbank.interview.domain;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.hoongoin.interviewbank.common.gpt.GptRequestBody;
import org.hoongoin.interviewbank.common.gpt.GptRequestHandler;
import org.hoongoin.interviewbank.common.gpt.GptResponseBody;
import org.hoongoin.interviewbank.common.gpt.MessageRole;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.QuestionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionCommandServiceAsync {

	private final GptRequestHandler gptRequestHandler;
	private final QuestionRepository questionRepository;

	@Async
	@Transactional
	public void updateAllGptAnswerByQuestionIdAsync(List<QuestionEntity> questionEntities) {
		//TODO: N+1 problem

		log.info("QuestionCommandServiceAsync.updateGptAnswersAsync() is called.");

		GptRequestBody.Message assistantMessage = new GptRequestBody.Message(MessageRole.ASSISTANT.getRole(),
			"You are a Interview Q&A Assistant.");

		questionEntities.forEach(questionEntity -> {
			GptRequestBody.Message questionMessage = new GptRequestBody.Message(MessageRole.USER.getRole(),
				questionEntity.getContent());
			GptResponseBody gptResponseBody = gptRequestHandler.sendChatCompletionRequest(
				List.of(assistantMessage, questionMessage));

			questionEntity.modifyGptAnswer(gptResponseBody.getChoices().get(0).getMessage().getContent());
			log.info(gptResponseBody.getChoices().get(0).getMessage().getContent());
		});

		questionRepository.saveAll(questionEntities);
		log.info("QuestionCommandServiceAsync.updateGptAnswersAsync() is finished.");
		CompletableFuture.completedFuture(null);
	}
}
