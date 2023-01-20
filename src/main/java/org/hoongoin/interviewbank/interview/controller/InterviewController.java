package org.hoongoin.interviewbank.interview.controller;

import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.service.InterviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	@PostMapping
	public ResponseEntity<Long> createInterview(@RequestBody CreateInterviewRequest createInterviewRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(interviewService.createInterviewByCreateInterviewRequest(createInterviewRequest));
	}

	@PutMapping("/{id}")
	public ResponseEntity<UpdateInterviewResponse> updateInterview(
		@RequestBody UpdateInterviewRequest updateInterviewRequest, @PathVariable("id") long interviewId) {
		return ResponseEntity.ok(
			interviewService.updateInterviewResponseByUpdateInterviewRequest(updateInterviewRequest, interviewId));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Long> deleteInterview(@PathVariable("id") long interviewId) {
		return ResponseEntity.ok(interviewService.deleteInterviewById(interviewId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<FindInterviewResponse> findInterview(@PathVariable("id") long interviewId) {
		return ResponseEntity.ok(interviewService.findInterviewById(interviewId));
	}
}
