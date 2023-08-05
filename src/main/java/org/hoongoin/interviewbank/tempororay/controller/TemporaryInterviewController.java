package org.hoongoin.interviewbank.tempororay.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import org.hoongoin.interviewbank.tempororay.application.TemporaryInterviewService;
import org.hoongoin.interviewbank.tempororay.controller.request.CreateTemporaryInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.tempororay.controller.response.DeleteTemporaryInterviewAndQuestionResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.FindTemporaryInterviewByIdResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.CreateTemporaryInterviewAndQuestionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("temporary")
@RequiredArgsConstructor
public class TemporaryInterviewController {

	private final TemporaryInterviewService temporaryInterviewService;

	@PostMapping("/interview")
	public ResponseEntity<CreateTemporaryInterviewAndQuestionResponse> createTemporaryInterviewAndQuestion(@RequestBody
	CreateTemporaryInterviewAndQuestionsRequest request) {
		CreateTemporaryInterviewAndQuestionResponse createTemporaryInterviewAndQuestionResponse = temporaryInterviewService.createTemporaryInterviewAndQuestion(
			request, getRequestingAccountId());
		return ResponseEntity.status(HttpStatus.CREATED).body(createTemporaryInterviewAndQuestionResponse);
	}

	@GetMapping("/interview/{id}")
	public ResponseEntity<FindTemporaryInterviewByIdResponse> findTemporaryInterviewById(
		@PathVariable("id") long temporaryInterviewId) {
		return ResponseEntity.ok(temporaryInterviewService.findTemporaryInterviewById(temporaryInterviewId));
	}

	@DeleteMapping("/interview/{id}")
	public ResponseEntity<DeleteTemporaryInterviewAndQuestionResponse> deleteTemporaryInterviewAndQuestion(
		@PathVariable("id") long temporaryInterviewId) {
		return ResponseEntity.ok(temporaryInterviewService.deleteTemporaryInterviewAndQuestion(temporaryInterviewId,
			getRequestingAccountId()));
	}
}
