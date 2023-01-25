package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {

	private final InterviewCommandService interviewCommandService;
	private final InterviewQueryService interviewQueryService;
	private final InterviewMapper interviewMapper;
	private final QuestionCommandService questionCommandService;

	@Transactional
	public long createInterviewByCreateInterviewRequest(CreateInterviewRequest createInterviewRequest) {
		return interviewCommandService.insertInterview(
			new Interview(createInterviewRequest.getTitle(), createInterviewRequest.getAccountId()));
	}

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByUpdateInterviewRequest(
		UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		return interviewMapper.interviewToUpdateInterviewResponse(
			interviewCommandService.updateInterview(updateInterviewRequest, interviewId));
	}

	@Transactional
	public long deleteInterviewById(long interviewId) {
		interviewCommandService.deleteInterview(interviewId);
		questionCommandService.deleteQuestionsByInterviewId(interviewId);
		return interviewId;
	}

	@Transactional
	public CreateInterviewAndQuestionsResponse createInterviewAndQuestionsByRequest(
		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest) {
		long createdInterviewId = interviewCommandService.insertInterview(
			new Interview(createInterviewAndQuestionsRequest.getTitle(),
				createInterviewAndQuestionsRequest.getAccountId()));

		Interview createdInterview = interviewQueryService.findEntityById(createdInterviewId);

		List<Question> questions = questionCommandService.insertQuestions(
			createInterviewAndQuestionsRequest.getQuestionsRequest(), createdInterviewId);

		List<String> questionContents = new ArrayList<>();
		List<Long> questionIds = new ArrayList<>();

		questions.forEach(question -> {
			questionContents.add(question.getContent());
			questionIds.add(question.getQuestionId());
		});

		return new CreateInterviewAndQuestionsResponse(createInterviewAndQuestionsRequest.getTitle(),
			createdInterviewId, questionContents, questionIds, createdInterview.getCreatedAt());
	}

	@Transactional(readOnly = true)
	public FindInterviewResponse findInterviewById(long interviewId) {
		Interview interview = interviewQueryService.findEntityById(interviewId);
		return new FindInterviewResponse(interview.getInterviewId(), interview.getTitle(), interview.getAccountId(),
			interview.getCreatedAt(), interview.getUpdatedAt(), interview.getDeletedAt(), interview.getDeletedFlag());
	}
}
