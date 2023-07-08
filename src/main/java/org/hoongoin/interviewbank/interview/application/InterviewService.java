package org.hoongoin.interviewbank.interview.application;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.InterviewMapper;
import org.hoongoin.interviewbank.common.dto.PageDto;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
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
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

		Interview interviewToCreate = interviewMapper.createInterviewAndQuestionsRequestToInterview(
			createInterviewAndQuestionsRequest, accountId);
		Interview createdInterview = interviewCommandService.insertInterview(interviewToCreate);

		JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(
			createInterviewAndQuestionsRequest.getJobCategoryId());

		List<Question> questions = interviewMapper.createInterviewAndQuestionsRequestToQuestions(
			createInterviewAndQuestionsRequest, createdInterview.getInterviewId());

		List<Question> insertedQuestions = questionCommandService.insertQuestions(questions,
			createdInterview.getInterviewId());

		return makeCreateInterviewAndQuestionsResponse(createdInterview, jobCategory, insertedQuestions);
	}

	@Transactional
	public UpdateInterviewResponse updateInterviewResponseByRequestAndInterviewId(
		UpdateInterviewRequest updateInterviewRequest, long interviewId, long accountId) {

		validateQuestionsSize(updateInterviewRequest.getQuestions().size());

		Interview interviewToUpdate = interviewMapper.updateInterviewRequestToInterview(updateInterviewRequest,
			interviewId, accountId);
		Interview interview = interviewCommandService.updateInterview(interviewToUpdate, interviewId, accountId);

		JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(
			updateInterviewRequest.getJobCategoryId());

		List<Question> newQuestions = interviewMapper.updateInterviewRequestToQuestions(updateInterviewRequest,
			interviewId);
		List<Question> updatedQuestions = questionCommandService.updateQuestions(interviewId, newQuestions);

		return makeUpdateInterviewResponse(interview, jobCategory, updatedQuestions);
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
		JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(interview.getJobCategoryId());

		List<Question> questions = questionQueryService.findQuestionsByInterviewId(
			interview.getInterviewId());

		Account account = accountQueryService.findAccountByAccountId(interview.getAccountId());

		return makeFindInterviewResponse(interview, jobCategory, questions, account);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewPageByPageAndSize(int page, int size) {
		PageDto<Interview> interviews = interviewQueryService.findInterviewListByPageAndSize(page, size);
		return getFindInterviewPageResponse(interviews);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse searchInterview(String query, List<Long> jobCategories, Date startDate,
		Date endDate, InterviewPeriod interviewPeriod, CareerYear careerYear, int page, int size) {
		LocalDateTime startDateTime = startDate == null ? null : startDate.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate()
				.atStartOfDay();
		LocalDateTime endDateTime = endDate == null ? null : endDate.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate()
				.atTime(23, 59, 59);
		PageDto<Interview> interviews = interviewQueryService.searchInterview(query, jobCategories, startDateTime,
			endDateTime, interviewPeriod, careerYear, page, size);

		return getFindInterviewPageResponse(interviews);
	}

	@Transactional(readOnly = true)
	public FindInterviewPageResponse findInterviewsByAccountId(long requestingAccountId, int page, int size) {
		PageDto<Interview> interviews = interviewQueryService.findInterviewsByAccountIdAndPageAndSize(
			requestingAccountId,
			page, size);

		return getFindInterviewPageResponse(interviews);
	}

	private CreateInterviewAndQuestionsResponse makeCreateInterviewAndQuestionsResponse(Interview createdInterview,
		JobCategory jobCategory, List<Question> questions) {
		List<CreateInterviewAndQuestionsResponse.Question> createInterviewAndQuestionsResponseQuiestions = new ArrayList<>();
		questions.forEach(question -> createInterviewAndQuestionsResponseQuiestions.add(
			new CreateInterviewAndQuestionsResponse.Question(question.getContent(), question.getGptAnswer(),
				question.getQuestionId())));

		return CreateInterviewAndQuestionsResponse.builder()
			.title(createdInterview.getTitle())
			.interviewId(createdInterview.getInterviewId())
			.questions(createInterviewAndQuestionsResponseQuiestions)
			.interviewPeriod(createdInterview.getInterviewPeriod())
			.careerYear(createdInterview.getCareerYear())
			.jobCategory(interviewMapper.jobCategoryToJobCategoryRespnose(jobCategory))
			.view(createdInterview.getView())
			.build();
	}

	private UpdateInterviewResponse makeUpdateInterviewResponse(Interview interview, JobCategory jobCategory,
		List<Question> questions) {

		List<UpdateInterviewResponse.Question> updateInterviewResponseQuestions = new ArrayList<>();

		questions.forEach(question -> updateInterviewResponseQuestions.add(
			new UpdateInterviewResponse.Question(question.getQuestionId(), question.getContent(),
				question.getCreatedAt(), question.getUpdatedAt(), question.getGptAnswer())));

		return UpdateInterviewResponse.builder()
			.title(interview.getTitle())
			.createdAt(interview.getCreatedAt())
			.updatedAt(interview.getUpdatedAt())
			.questions(updateInterviewResponseQuestions)
			.interviewPeriod(interview.getInterviewPeriod())
			.careerYear(interview.getCareerYear())
			.jobCategory(interviewMapper.jobCategoryToJobCategoryRespnose(jobCategory))
			.build();
	}

	private FindInterviewResponse makeFindInterviewResponse(Interview interview, JobCategory jobCategory,
		List<Question> questions, Account account) {

		List<FindInterviewResponse.Question> findInterviewResponseQuestions = new ArrayList<>();
		questions.forEach(question -> findInterviewResponseQuestions.add(
			new FindInterviewResponse.Question(question.getQuestionId(), question.getContent(), question.getGptAnswer(),
				question.getCreatedAt(), question.getUpdatedAt(), question.getDeletedAt(), question.getDeletedFlag())));

		return FindInterviewResponse.builder()
			.interviewId(interview.getInterviewId())
			.title(interview.getTitle())
			.accountId(interview.getAccountId())
			.createdAt(interview.getCreatedAt())
			.updatedAt(interview.getUpdatedAt())
			.questions(findInterviewResponseQuestions)
			.interviewPeriod(interview.getInterviewPeriod())
			.careerYear(interview.getCareerYear())
			.jobCategory(interviewMapper.jobCategoryToJobCategoryRespnose(jobCategory))
			.view(interview.getView())
			.writerNickname(account.getNickname())
			.build();
	}

	private FindInterviewPageResponse getFindInterviewPageResponse(PageDto<Interview> interviewPageDto) {
		List<FindInterviewPageResponse.Interview> findInterviewPageResponseInterview = new ArrayList<>();

		for (Interview interview : interviewPageDto.getContent()) {
			JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(interview.getJobCategoryId());
			JobCategoryResponse jobCategoryResponse = interviewMapper.jobCategoryToJobCategoryRespnose(jobCategory);

			findInterviewPageResponseInterview.add(
				interviewMapper.interviewAndNicknameToFindInterviewPageResponseInterview(interview,
					findAccountByInterview(interview), jobCategoryResponse));
		}
		return new FindInterviewPageResponse(interviewPageDto.getTotalPages(), interviewPageDto.getTotalElements(),
				interviewPageDto.getCurrentPage(), interviewPageDto.getCurrentElements(), findInterviewPageResponseInterview);
	}

	private Account findAccountByInterview(Interview interview) {
		return accountQueryService.findAccountByAccountId(interview.getAccountId());
	}

	private void validateQuestionsSize(int questionSize) {
		if (questionSize > 1000 || questionSize < 1) {
			log.info("TemporaryQuestion Size Validation Failed");
			throw new IbValidationException("TemporaryQuestion Size Validation Failed");
		}
	}
}
