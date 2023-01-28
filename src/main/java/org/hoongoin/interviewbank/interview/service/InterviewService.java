package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.DeleteInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
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
	private final QuestionQueryService questionQueryService;

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByRequestAndInterviewId(
		UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		Interview interview = interviewCommandService.updateInterview(new Interview(updateInterviewRequest.getTitle()),
			interviewId);

		List<Question> newQuestions = interviewMapper.updateInterviewRequestToQuestions(updateInterviewRequest, interviewId);

		List<Question> updatedQuestions = questionCommandService.updateQuestions(newQuestions);

		return interviewMapper.questionsAndTitleToUpdateInterviewResponse(updatedQuestions, interview.getTitle());
	}

	@Transactional
	public DeleteInterviewResponse deleteInterviewById(long interviewId) {
		long deletedInterviewId = interviewCommandService.deleteInterview(interviewId);
		List<Long> deletedQuestionIds = questionCommandService.deleteQuestionsByInterviewId(interviewId);
		return new DeleteInterviewResponse(deletedInterviewId, deletedQuestionIds);
	}

	@Transactional
	public CreateInterviewAndQuestionsResponse createInterviewAndQuestionsByRequest(
		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest) {
		long createdInterviewId = interviewCommandService.insertInterview(
			new Interview(createInterviewAndQuestionsRequest.getTitle(),
				createInterviewAndQuestionsRequest.getAccountId()));

		Interview createdInterview = interviewQueryService.findInterviewById(createdInterviewId);

		List<Question> questions = questionCommandService.insertQuestions(
			interviewMapper.createInterviewAndQuestionsRequestToQuestions(createInterviewAndQuestionsRequest,
				createdInterviewId), createdInterviewId);

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
		Interview interview = interviewQueryService.findInterviewById(interviewId);

		List<Question> questions = questionQueryService.findQuestionsByInterviewId(
			interview.getInterviewId());

		return interviewMapper.questionListAndInterviewToFindInterviewResponse(questions, interview);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewPageByPageAndSize(int page, int size) {
		List<Interview> interviews = interviewQueryService.findInterviewListByPageAndSize(page, size);

		List<FindInterviewPageResponse.Interview> findInterviewPageResponseInterview = new ArrayList<>();

		interviews.forEach(interview -> findInterviewPageResponseInterview.add(
			interviewMapper.interviewAndNicknameToFindInterviewPageResponseInterview(interview)));

		return new FindInterviewPageResponse(findInterviewPageResponseInterview);
	}
}