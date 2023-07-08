package org.hoongoin.interviewbank.interview.controller;

import org.hoongoin.interviewbank.interview.application.InterviewLikeService;
import org.hoongoin.interviewbank.interview.controller.request.LikeInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.LikeInterviewResponse;
import org.hoongoin.interviewbank.utils.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("interview")
@RequiredArgsConstructor
public class InterviewLikeController {

	private final InterviewLikeService interviewLikeService;

	@PostMapping("/{interview-id}/like")
	public ResponseEntity<LikeInterviewResponse> likeInterview(@PathVariable("interview-id") long interviewId,
		@RequestBody LikeInterviewRequest likeInterviewRequest) {
		long requestingAccountId = SecurityUtil.getRequestingAccountId();
		LikeInterviewResponse likeInterviewResponse = interviewLikeService.likeInterview(interviewId,
			likeInterviewRequest, requestingAccountId);
		return ResponseEntity.ok().body(likeInterviewResponse);
	}

}
