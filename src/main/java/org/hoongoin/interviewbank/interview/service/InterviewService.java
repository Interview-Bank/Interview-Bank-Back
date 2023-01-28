package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.service.AccountQueryService;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
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
	private final AccountQueryService accountQueryService;

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByRequestAndInterviewId(
		UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		Interview interview = interviewCommandService.updateInterview(updateInterviewRequest, interviewId);

		List<Question> questions = questionCommandService.updateQuestions(updateInterviewRequest);

		List<UpdateInterviewResponse.Question> updatedQuestions = new ArrayList<>();

		questions.forEach(question -> updatedQuestions.add(
			new UpdateInterviewResponse.Question(question.getQuestionId(), question.getInterviewId(),
				question.getContent(), question.getUpdatedAt())));

		return new UpdateInterviewResponse(interview.getTitle(), updatedQuestions);
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
		List<Question> questions = questionQueryService.findQuestionsByInterviewId(
			interview.getInterviewId());

		return interviewMapper.questionListAndInterviewToFindInterviewResponse(questions, interview);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewPageByPageAndSize(int page, int size) {
		List<Interview> interviews = interviewQueryService.findEntitiesPageByPageAndSize(page, size);
		List<FindInterviewPageResponse.Interview> findInterviewPageResponseInterview = new ArrayList<>();
		interviews.forEach(interview -> findInterviewPageResponseInterview.add(
			interviewMapper.interviewAndNicknameToFindInterviewPageResponseInterview(interview)));
		return new FindInterviewPageResponse(findInterviewPageResponseInterview);
	}
}