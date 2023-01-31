package org.hoongoin.interviewbank.interview.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.service.AccountQueryService;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
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
	private final AccountQueryService accountQueryService;

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByRequestAndInterviewId(
		UpdateInterviewRequest updateInterviewRequest, long interviewId) {
		Interview interview = interviewCommandService.updateInterview(new Interview(updateInterviewRequest.getTitle()),
			interviewId);

		List<Question> newQuestions = interviewMapper.updateInterviewRequestToQuestions(updateInterviewRequest,
			interviewId);

		List<Question> updatedQuestions = questionCommandService.updateQuestions(newQuestions);

		UpdateInterviewResponse updateInterviewResponse = interviewMapper.questionsAndTitleToUpdateInterviewResponse(
			updatedQuestions, interview.getTitle());

		validateQuestionsSize(updateInterviewResponse.getQuestions().size());

		return updateInterviewResponse;
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

		List<CreateInterviewAndQuestionsResponse.Question> createInterviewAndQuestionsResponseQuiestions = new ArrayList<>();

		questions.forEach(question -> createInterviewAndQuestionsResponseQuiestions.add(
			new CreateInterviewAndQuestionsResponse.Question(question.getContent(), question.getInterviewId())));

		CreateInterviewAndQuestionsResponse createInterviewAndQuestionsResponse = new CreateInterviewAndQuestionsResponse(
			createdInterview.getTitle(),
			createdInterviewId, createInterviewAndQuestionsResponseQuiestions, createdInterview.getCreatedAt());

		validateQuestionsSize(createInterviewAndQuestionsResponse.getQuestions().size());

		return createInterviewAndQuestionsResponse;
	}

	@Transactional(readOnly = true)
	public FindInterviewResponse findInterviewById(long interviewId) {
		Interview interview = interviewQueryService.findInterviewById(interviewId);

		List<Question> questions = questionQueryService.findQuestionsByInterviewId(
			interview.getInterviewId());

		validateQuestionsSize(questions.size());

		return interviewMapper.questionListAndInterviewToFindInterviewResponse(questions, interview);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewPageByPageAndSize(int page, int size) {
		List<Interview> interviews = interviewQueryService.findInterviewListByPageAndSize(page, size);

		Account account = findAccountByInterviews(interviews);

		List<FindInterviewPageResponse.Interview> findInterviewPageResponseInterview = new ArrayList<>();

		interviews.forEach(interview -> findInterviewPageResponseInterview.add(
			interviewMapper.interviewAndNicknameToFindInterviewPageResponseInterview(interview, account)));

		return new FindInterviewPageResponse(findInterviewPageResponseInterview);
	}

	private Account findAccountByInterviews(List<Interview> interviews) {
		if (interviews.isEmpty()) {
			throw new IbEntityNotFoundException("Account");
		}
		return accountQueryService.findAccountByAccountId(interviews.get(0).getAccountId());
	}

	private void validateQuestionsSize(int questionSize) {
		if (questionSize > 1000 || questionSize < 1) {
			throw new IbValidationException("Question size");
		}
	}
}
