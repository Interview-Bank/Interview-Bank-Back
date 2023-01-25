package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.interview.controller.request.UpdateQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindQuestionsByInterviewIdResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateQuestionsResponse;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class QuestionService {

	private final QuestionCommandService questionCommandService;
	private final QuestionQueryService questionQueryService;

	@Transactional(readOnly = true)
	public FindQuestionsByInterviewIdResponse findQuestionsByInterviewId(long interviewId) {
		return new FindQuestionsByInterviewIdResponse(questionQueryService.findQuestionsByInterviewId(interviewId));
	}

	@Transactional
	public UpdateQuestionsResponse updateQuestionsByRequestAndInterviewId(UpdateQuestionsRequest updateQuestionsRequest,
		long interviewId) {
		List<Question> questions = questionCommandService.updateQuestions(updateQuestionsRequest, interviewId);

		List<UpdateQuestionsResponse.Question> updatedQuestions = new ArrayList<>();

		questions.forEach(question -> updatedQuestions.add(new UpdateQuestionsResponse.Question(question.getQuestionId(), question.getInterviewId(), question.getContent(), question.getUpdatedAt())));
		return new UpdateQuestionsResponse(updatedQuestions);
	}

	public Long deleteQuestionByQuestionId(long questionId) {
		return questionCommandService.deleteQuestion(questionId).getQuestionId();
	}
}
