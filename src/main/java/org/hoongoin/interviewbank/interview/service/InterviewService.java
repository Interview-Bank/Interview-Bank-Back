package org.hoongoin.interviewbank.interview.service;

import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {

	private final InterviewCommandService interviewCommandService;
	private final InterviewQueryService interviewQueryService;
	private final InterviewMapper interviewMapper;

	public long createInterviewByCreateInterviewRequest(CreateInterviewRequest createInterviewRequest) {
		return interviewCommandService.insertInterview(createInterviewRequest);
	}

	public UpdateInterviewResponse updateInterviewResponseByUpdateInterviewRequest(UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		return interviewMapper.interviewToUpdateInterviewResponse(interviewCommandService.updateInterview(updateInterviewRequest, interviewId));
	}

	public long deleteInterviewById(long interviewId) {
		return interviewCommandService.deleteInterview(interviewId);
	}

	public FindInterviewResponse findInterviewById(long interviewId) {
		return interviewMapper.interviewToFindInterviewResponse(interviewQueryService.findEntityById(interviewId));
	}
}
