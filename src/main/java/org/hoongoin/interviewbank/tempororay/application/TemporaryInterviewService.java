package org.hoongoin.interviewbank.tempororay.application;

import java.util.List;

import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import org.hoongoin.interviewbank.interview.domain.JobCategoryQueryService;
import org.hoongoin.interviewbank.tempororay.TemporaryMapper;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryInterview;
import org.hoongoin.interviewbank.tempororay.application.entity.TemporaryQuestion;
import org.hoongoin.interviewbank.tempororay.controller.request.CreateTemporaryInterviewAndQuestionsRequest;
import org.hoongoin.interviewbank.tempororay.controller.response.DeleteTemporaryInterviewAndQuestionResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.FindTemporaryInterviewByIdResponse;
import org.hoongoin.interviewbank.tempororay.controller.response.CreateTemporaryInterviewAndQuestionResponse;
import org.hoongoin.interviewbank.tempororay.domain.TemporaryInterviewCommandService;
import org.hoongoin.interviewbank.tempororay.domain.TemporaryInterviewQueryService;
import org.hoongoin.interviewbank.tempororay.domain.TemporaryQuestionCommandService;
import org.hoongoin.interviewbank.tempororay.domain.TemporaryQuestionQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TemporaryInterviewService {

	private final TemporaryMapper temporaryMapper;
	private final TemporaryInterviewCommandService temporaryInterviewCommandService;
	private final TemporaryQuestionCommandService temporaryQuestionCommandService;
	private final TemporaryInterviewQueryService temporaryInterviewQueryService;
	private final TemporaryQuestionQueryService temporaryQuestionQueryService;
	private final JobCategoryQueryService jobCategoryQueryService;

	public CreateTemporaryInterviewAndQuestionResponse createTemporaryInterviewAndQuestion(
		CreateTemporaryInterviewAndQuestionsRequest createTemporaryInterviewAndQuestionRequest,
		long requestingAccountId) {
		if (createTemporaryInterviewAndQuestionRequest.getInterviewId() != null) {
			temporaryInterviewCommandService.deleteTemporaryInterview(
				createTemporaryInterviewAndQuestionRequest.getInterviewId());
		}

		TemporaryInterview temporaryInterview = temporaryMapper.createTemporaryInterviewAndQuestionRequestToTemporaryInterview(
			createTemporaryInterviewAndQuestionRequest, requestingAccountId);

		Long createdTemporaryInterviewId = temporaryInterviewCommandService.insertTemporaryInterview(
			temporaryInterview);

		List<TemporaryQuestion> temporaryQuestions = temporaryMapper.createTemporaryInterviewAndQuestionsToTemporaryQuestions(
			createTemporaryInterviewAndQuestionRequest, createdTemporaryInterviewId);

		List<Long> insertedTemporaryQuestions = temporaryQuestionCommandService.insertTemporaryQuestions(
			temporaryQuestions, createdTemporaryInterviewId);

		return new CreateTemporaryInterviewAndQuestionResponse(createdTemporaryInterviewId, insertedTemporaryQuestions);
	}

	@Transactional(readOnly = true)
	public FindTemporaryInterviewByIdResponse findTemporaryInterviewById(long temporaryInterviewId) {
		TemporaryInterview temporaryInterview = temporaryInterviewQueryService.findTemporaryInterviewById(
			temporaryInterviewId);

		List<TemporaryQuestion> temporaryQuestions = temporaryQuestionQueryService.findTemporaryQuestionByTemporaryInterviewId(
			temporaryInterviewId);

		if (temporaryInterview.getJobCategoryId() != null) {
			JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(
				temporaryInterview.getJobCategoryId());
			return makeTemporaryInterviewByIdResponse(temporaryInterview, temporaryQuestions,
				new JobCategoryResponse(jobCategory.getJobCategoryId(), jobCategory.getFirstLevelName(),
					jobCategory.getSecondLevelName()));
		}

		return makeTemporaryInterviewByIdResponse(temporaryInterview, temporaryQuestions, null);
	}

	private FindTemporaryInterviewByIdResponse makeTemporaryInterviewByIdResponse(TemporaryInterview temporaryInterview,
		List<TemporaryQuestion> temporaryQuestions, JobCategoryResponse jobCategoryResponse) {
		return FindTemporaryInterviewByIdResponse.builder()
			.temporaryInterviewId(temporaryInterview.getTemporaryInterviewId())
			.title(temporaryInterview.getTitle())
			.accountId(temporaryInterview.getAccountId())
			.createdAt(temporaryInterview.getCreatedAt())
			.updatedAt(temporaryInterview.getUpdatedAt())
			.temporaryQuestions(temporaryMapper.temporaryQuestionsToTemporaryQuestionResponse(temporaryQuestions))
			.interviewPeriod(temporaryInterview.getInterviewPeriod())
			.careerYear(temporaryInterview.getCareerYear())
			.jobCategoryResponse(jobCategoryResponse)
			.build();
	}

	public DeleteTemporaryInterviewAndQuestionResponse deleteTemporaryInterviewAndQuestion(long temporaryInterviewId,
		long accountId) {
		TemporaryInterview temporaryInterview = temporaryInterviewQueryService.findTemporaryInterviewById(
			temporaryInterviewId);

		List<TemporaryQuestion> temporaryQuestions = temporaryQuestionQueryService.findTemporaryQuestionByTemporaryInterviewId(
			temporaryInterviewId);

		isMatchInterviewAndAccount(accountId, temporaryInterview);

		temporaryInterviewCommandService.deleteTemporaryInterview(temporaryInterview.getTemporaryInterviewId());
		temporaryQuestionCommandService.deleteTemporaryQuestionsByTemporaryInterviewId(
			temporaryInterview.getTemporaryInterviewId());

		return new DeleteTemporaryInterviewAndQuestionResponse(temporaryInterview.getTemporaryInterviewId(),
			temporaryMapper.temporaryQuestionsToTemporaryQuestionIds(temporaryQuestions));
	}

	private void isMatchInterviewAndAccount(long accountId, TemporaryInterview temporaryInterview) {
		if (temporaryInterview.getAccountId() != accountId) {
			log.info("Interview Writer Account And Request Account Do Not Match");
			throw new IbAccountNotMatchException("Bad Request");
		}
	}
}
