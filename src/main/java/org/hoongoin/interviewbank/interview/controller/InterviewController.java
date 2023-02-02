package org.hoongoin.interviewbank.interview.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.DeleteInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	@Secured("ROLE_USER")
	@PutMapping("/{interview-id}")
	public ResponseEntity<UpdateInterviewResponse> updateInterview(
		@RequestBody UpdateInterviewRequest updateInterviewRequest, @PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(
			interviewService.updateInterviewResponseByRequestAndInterviewId(updateInterviewRequest, interviewId));
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/{interview-id}")
	public ResponseEntity<DeleteInterviewResponse> deleteInterview(@PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(interviewService.deleteInterviewById(interviewId));
	}

	@GetMapping("/{interview-id}")
	public ResponseEntity<FindInterviewResponse> findInterview(@PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(interviewService.findInterviewById(interviewId));
	}

	@Secured("ROLE_USER")
	@PostMapping
	public ResponseEntity<CreateInterviewAndQuestionsResponse> createInterviewAndQuestions(
		@RequestBody CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest) {
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, getRequestingAccountId());

		return ResponseEntity.status(HttpStatus.CREATED).body(createInterviewAndQuestionsResponse);
	}

	@GetMapping
	public ResponseEntity<FindInterviewPageResponse> findInterviewPage(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		return ResponseEntity.ok(interviewService.findInterviewPageByPageAndSize(page, size));
	}
}