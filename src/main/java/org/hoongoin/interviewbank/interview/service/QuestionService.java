package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.interview.controller.response.FindQuestionsByInterviewIdResponse;
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

	public Long deleteQuestionByQuestionId(long questionId) {
		return questionCommandService.deleteQuestion(questionId).getQuestionId();
	}
}