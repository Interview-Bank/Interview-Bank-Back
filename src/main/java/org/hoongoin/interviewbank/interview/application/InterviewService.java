package org.hoongoin.interviewbank.interview.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.interview.controller.request.CreateInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.interview.controller.response.CreateInterviewAndQuestionsResponse;
import org.hoongoin.interviewbank.interview.controller.request.UpdateInterviewRequest;
import org.hoongoin.interviewbank.interview.controller.response.DeleteInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewPageResponse;
import org.hoongoin.interviewbank.interview.controller.response.FindInterviewResponse;
import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import org.hoongoin.interviewbank.interview.controller.response.UpdateInterviewResponse;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.hoongoin.interviewbank.interview.domain.InterviewCommandService;
import org.hoongoin.interviewbank.interview.domain.InterviewQueryService;
import org.hoongoin.interviewbank.interview.domain.JobCategoryQueryService;
import org.hoongoin.interviewbank.interview.domain.QuestionCommandService;
import org.hoongoin.interviewbank.interview.domain.QuestionQueryService;
import org.hoongoin.interviewbank.interview.infrastructure.entity.FullJobCategory;
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
	private final JobCategoryQueryService jobCategoryQueryService;

	@Transactional
	public CreateInterviewAndQuestionsResponse createInterviewAndQuestionsByRequest(
		CreateInterviewAndQuestionsRequest createInterviewAndQuestionsRequest, long accountId) {

		validateQuestionsSize(createInterviewAndQuestionsRequest.getQuestionsRequest().getQuestions().size());

		Interview createdInterview = interviewCommandService.insertInterview(
			Interview.builder()
				.title(createInterviewAndQuestionsRequest.getTitle())
				.jobCategoryId(createInterviewAndQuestionsRequest.getJobCategoryId())
				.accountId(accountId)
				.interviewPeriod(createInterviewAndQuestionsRequest.getInterviewPeriod())
				.careerYear(createInterviewAndQuestionsRequest.getCareerYear())
				.build());

		FullJobCategory fullJobCategory;
		if(createInterviewAndQuestionsRequest.getJobCategoryId() != null) {
			fullJobCategory = jobCategoryQueryService.findFullJobCategoryById(createInterviewAndQuestionsRequest.getJobCategoryId());
		} else {
			fullJobCategory = new FullJobCategory();
		}

		List<Question> questions = questionCommandService.insertQuestions(
			interviewMapper.createInterviewAndQuestionsRequestToQuestions(createInterviewAndQuestionsRequest,
				createdInterview.getInterviewId()), createdInterview.getInterviewId());

		List<CreateInterviewAndQuestionsResponse.Question> createInterviewAndQuestionsResponseQuiestions = new ArrayList<>();

		questions.forEach(question -> createInterviewAndQuestionsResponseQuiestions.add(
			new CreateInterviewAndQuestionsResponse.Question(question.getContent(), question.getInterviewId())));
		JobCategoryResponse jobCategoryResponse = new JobCategoryResponse(fullJobCategory.getJobCategoryId(),
			fullJobCategory.getFirstLevelName(), fullJobCategory.getSecondLevelName());

		return new CreateInterviewAndQuestionsResponse(
			createdInterview.getTitle(), createdInterview.getInterviewId(),
			createInterviewAndQuestionsResponseQuiestions,
			createdInterview.getCreatedAt(), createdInterview.getInterviewPeriod(), createdInterview.getCareerYear(),
			jobCategoryResponse);
	}

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByRequestAndInterviewId(
		UpdateInterviewRequest updateInterviewRequest, long interviewId, long accountId) {

		validateQuestionsSize(updateInterviewRequest.getQuestions().size());

		Interview interview = interviewCommandService.updateInterview(
			Interview.builder()
				.title(updateInterviewRequest.getTitle())
				.jobCategoryId(updateInterviewRequest.getJobCategoryId())
				.interviewPeriod(updateInterviewRequest.getInterviewPeriod())
				.careerYear(updateInterviewRequest.getCareerYear())
				.build(),
			interviewId, accountId);

		FullJobCategory fullJobCategory;
		if(updateInterviewRequest.getJobCategoryId() != null) {
			fullJobCategory = jobCategoryQueryService.findFullJobCategoryById(updateInterviewRequest.getJobCategoryId());
		} else {
			fullJobCategory = new FullJobCategory();
		}

		List<Question> newQuestions = interviewMapper.updateInterviewRequestToQuestions(updateInterviewRequest,
			interviewId);

		List<Question> updatedQuestions = questionCommandService.updateQuestions(newQuestions);

		return interviewMapper.questionsAndTitleToUpdateInterviewResponse(updatedQuestions, interview.getTitle(),
			interview.getJobCategoryId(), interview.getInterviewPeriod(), interview.getCareerYear(), fullJobCategory);
	}

	@Transactional
	public DeleteInterviewResponse deleteInterviewById(long interviewId, long accountId) {
		long deletedInterviewId = interviewCommandService.deleteInterview(interviewId, accountId);

		List<Long> deletedQuestionIds = questionCommandService.deleteQuestionsByInterviewId(interviewId);

		return new DeleteInterviewResponse(deletedInterviewId, deletedQuestionIds);
	}

	@Transactional(readOnly = true)
	public FindInterviewResponse findInterviewById(long interviewId) {
		Interview interview = interviewQueryService.findInterviewById(interviewId);
		FullJobCategory fullJobCategory;
		if(interview.getJobCategoryId() != null) {
			fullJobCategory = jobCategoryQueryService.findFullJobCategoryById(interview.getJobCategoryId());
		} else {
			fullJobCategory = new FullJobCategory();
		}

		List<Question> questions = questionQueryService.findQuestionsByInterviewId(
			interview.getInterviewId());

		validateQuestionsSize(questions.size());

		JobCategoryResponse jobCategoryResponse = interviewMapper.fullJobCategoryToJobCategoryResponse(fullJobCategory);
		return interviewMapper.questionListAndInterviewToFindInterviewResponse(questions, interview, jobCategoryResponse);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewPageByPageAndSize(int page, int size) {
		List<Interview> interviews = interviewQueryService.findInterviewListByPageAndSize(page, size);

		return getFindInterviewPageResponse(interviews);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse searchInterview(String query, String primaryJobCategory,
		String secondaryJobCategory, Date startDate, Date endDate, int page, int size) {
		Long jobCategoryId = jobCategoryQueryService.findJobCategoryIdByJobCategoryName(primaryJobCategory,
			secondaryJobCategory);
		List<Interview> interviews = interviewQueryService.searchInterview(query, jobCategoryId, startDate, endDate,
			page, size);

		return getFindInterviewPageResponse(interviews);
	}

	private FindInterviewPageResponse getFindInterviewPageResponse(List<Interview> interviews) {
		List<FindInterviewPageResponse.Interview> findInterviewPageResponseInterview = new ArrayList<>();
		for(Interview interview: interviews){
			FullJobCategory fullJobCategory;
			if(interview.getJobCategoryId() != null) {
				fullJobCategory = jobCategoryQueryService.findFullJobCategoryById(interview.getJobCategoryId());
			} else {
				fullJobCategory = new FullJobCategory();
			}

			JobCategoryResponse jobCategoryResponse = interviewMapper.fullJobCategoryToJobCategoryResponse(fullJobCategory);
			findInterviewPageResponseInterview.add(
				interviewMapper.interviewAndNicknameToFindInterviewPageResponseInterview(interview,
					findAccountByInterview(interview), jobCategoryResponse));

		}
		return new FindInterviewPageResponse(findInterviewPageResponseInterview);
	}

	private Account findAccountByInterview(Interview interview) {
		return accountQueryService.findAccountByAccountId(interview.getAccountId());
	}

	private void validateQuestionsSize(int questionSize) {
		if (questionSize > 1000 || questionSize < 1) {
			throw new IbValidationException("Question Size");
		}
	}
}
