package org.hoongoin.interviewbank.interview.controller;

import org.hoongoin.interviewbank.interview.controller.response.FindQuestionsByInterviewIdResponse;
import org.hoongoin.interviewbank.interview.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("questions")
@RequiredArgsConstructor
public class QuestionController {

	private final QuestionService questionService;

	@GetMapping("/{interviewId}")
	public ResponseEntity<FindQuestionsByInterviewIdResponse> findQuestionsByInterviewId(
		@PathVariable("interviewId") long interviewId) {
		return ResponseEntity.ok(questionService.findQuestionsByInterviewId(interviewId));
	}

	@DeleteMapping("/{questionId}")
	public ResponseEntity<Long> deleteQuestion(@PathVariable("questionId") long questionId) {
		return ResponseEntity.ok(questionService.deleteQuestionByQuestionId(questionId));
	}
}