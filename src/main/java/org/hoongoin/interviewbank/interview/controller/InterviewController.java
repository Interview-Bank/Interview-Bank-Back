package org.hoongoin.interviewbank.interview.controller;

import static org.hoongoin.interviewbank.utils.SecurityUtil.*;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.DeleteInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.application.InterviewService;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@Slf4j
@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewController {

	private final InterviewService interviewService;

	@PostMapping
	public ResponseEntity<CreateInterviewAndQuestionsResponse> createInterviewAndQuestions(
		@RequestBody CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest) {
		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = interviewService.createInterviewAndQuestionsByRequest(
			createInterviewAndQuestionsRequest, getRequestingAccountId());
		log.info("createInterviewAndQuestionsResponse: {}", createInterviewAndQuestionsResponse);
		return ResponseEntity.status(HttpStatus.CREATED).body(createInterviewAndQuestionsResponse);
	}

	@GetMapping
	public ResponseEntity<FindInterviewPageResponse> findInterviewPage(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		return ResponseEntity.ok(interviewService.findInterviewPageByPageAndSize(page, size));
	}

	@GetMapping("/{interview-id}")
	public ResponseEntity<FindInterviewResponse> findInterview(@PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(interviewService.findInterviewById(interviewId));
	}

	@PutMapping("/{interview-id}")
	public ResponseEntity<UpdateInterviewResponse> updateInterview(
		@RequestBody UpdateInterviewRequest updateInterviewRequest, @PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(
			interviewService.updateInterviewResponseByRequestAndInterviewId(updateInterviewRequest, interviewId,
				getRequestingAccountId()));
	}

	@DeleteMapping("/{interview-id}")
	public ResponseEntity<DeleteInterviewResponse> deleteInterview(@PathVariable("interview-id") long interviewId) {
		return ResponseEntity.ok(interviewService.deleteInterviewById(interviewId, getRequestingAccountId()));
	}

	@GetMapping("/search")
	public ResponseEntity<FindInterviewPageResponse> searchInterview(
		@RequestParam(name = "query", required = false) String query,
		@RequestParam(name = "job-categories", required = false) List<Long> jobCategories,
		@RequestParam(name = "created-start-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
		@RequestParam(name = "created-end-date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
		@RequestParam(name = "interview-period", required = false) InterviewPeriod interviewPeriod,
		@RequestParam(name = "career-year", required = false) CareerYear careerYear,
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		return ResponseEntity.ok(
			interviewService.searchInterview(query, jobCategories, startDate, endDate, interviewPeriod, careerYear,
				page, size));
	}

	@GetMapping("/me")
	public ResponseEntity<FindInterviewPageResponse> findMyInterview(
		@RequestParam(name = "page", defaultValue = "0") int page,
		@RequestParam(name = "size", defaultValue = "10") int size) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.body(interviewService.findInterviewsByAccountId(requestingAccountId, page, size));
	}
}
