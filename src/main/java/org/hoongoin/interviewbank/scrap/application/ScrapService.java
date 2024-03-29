package org.hoongoin.interviewbank.scrap.application;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.common.dto.PageDto;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import org.hoongoin.interviewbank.interview.domain.InterviewQueryService;
import org.hoongoin.interviewbank.interview.domain.JobCategoryQueryService;
import org.hoongoin.interviewbank.interview.domain.QuestionQueryService;
import org.hoongoin.interviewbank.interview.application.entity.Interview;
import org.hoongoin.interviewbank.interview.application.entity.Question;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapWithScrapQuestionAndScrapAnswerList;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.OriginalInterviewResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapDetailResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapPageResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionAndScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionWithScrapAnswersResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionWithScrapAnswers;
import org.hoongoin.interviewbank.scrap.domain.ScrapAnswerCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQueryService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQuestionCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQuestionQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapService {

	private final ScrapCommandService scrapCommandService;
	private final ScrapQueryService scrapQueryService;
	private final ScrapQuestionCommandService scrapQuestionCommandService;
	private final ScrapQuestionQueryService scrapQuestionQueryService;
	private final AccountQueryService accountQueryService;
	private final InterviewQueryService interviewQueryService;
	private final QuestionQueryService questionQueryService;
	private final ScrapMapper scrapMapper;
	private final ScrapAnswerCommandService scrapAnswerCommandService;
	private final JobCategoryQueryService jobCategoryQueryService;

	@Transactional
	public CreateScrapResponse createScrapByCreateRequest(CreateScrapRequest createScrapRequest,
		long requestingAccountId) {
		Interview originalInterview = interviewQueryService.findInterviewById(createScrapRequest.getInterviewId());
		List<Question> originalQuestionsInInterview = questionQueryService.findQuestionsByInterviewId(
			createScrapRequest.getInterviewId());

		Account requestingAccount = accountQueryService.findAccountByAccountId(requestingAccountId);

		Scrap scrap = Scrap.builder()
			.interviewId(originalInterview.getInterviewId())
			.title(originalInterview.getTitle())
			.jobCategoryId(originalInterview.getJobCategoryId())
			.build();

		List<ScrapQuestion> scrapQuestions = new ArrayList<>();
		for (Question question : originalQuestionsInInterview) {
			scrapQuestions.add(
				ScrapQuestion.builder().scrapId(scrap.getScrapId()).content(question.getContent()).build());
		}

		ScrapWithScrapQuestionAndScrapAnswerList scrapWithScrapQuestionAndScrapAnswerList = scrapCommandService.insertScrapAndScrapQuestions(
			scrap, scrapQuestions, requestingAccount, originalInterview);

		return makeCreateScrapResponse(originalInterview, scrapWithScrapQuestionAndScrapAnswerList);
	}

	@Transactional
	public UpdateScrapResponse updateScrapByRequestAndScrapId(UpdateScrapRequest updateScrapRequest, long scrapId,
		long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccountId);

		scrap.setTitle(updateScrapRequest.getTitle());
		Scrap updatedScrap = scrapCommandService.updateScrap(scrap);
		return scrapMapper.scrapToUpdateScrapResponse(updatedScrap);
	}

	@Transactional
	public void deleteScrapByRequestAndScrapId(long scrapId, long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccountId);

		scrapAnswerCommandService.deleteAllScrapAnswerByScrapId(scrapId);
		scrapQuestionCommandService.deleteAllScrapQuestionByScrapId(scrapId);
		scrapCommandService.deleteScrapById(scrapId);
	}

	@Transactional(readOnly = true)
	public ReadScrapDetailResponse readScrapDetailById(long scrapId, long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccountId);

		Interview interview = interviewQueryService.findInterviewById(scrap.getInterviewId());

		List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers = scrapQuestionQueryService
			.findAllScrapQuestionWithScrapAnswersByScrapId(scrapId);

		return makeReadScrapResponse(scrap, interview, scrapQuestionsWithScrapAnswers);
	}

	@Transactional(readOnly = true)
	public ReadScrapPageResponse readScrapByRequestingAccountIdAndPageAndSize(long requestingAccountId, int page,
		int size) {
		PageDto<Scrap> scrapPageDto = scrapQueryService.findScrapAllByScrapWriterAccountIdAndPageAndSize(
			requestingAccountId,
			page, size);
		Account scrapWriterAccount = accountQueryService.findAccountByAccountId(requestingAccountId);

		List<ReadScrapPageResponse.Scrap> readScrapPageResponseScraps = new ArrayList<>();

		scrapPageDto.getContent().forEach(scrap ->
			{
				JobCategory jobCategory = jobCategoryQueryService.findJobCategoryById(scrap.getJobCategoryId());
				JobCategoryResponse jobCategoryResponse = new JobCategoryResponse(jobCategory.getJobCategoryId(),
					jobCategory.getFirstLevelName(), jobCategory.getSecondLevelName());
				readScrapPageResponseScraps.add(
					ReadScrapPageResponse.Scrap.builder()
						.scrapId(scrap.getScrapId())
						.title(scrap.getTitle())
						.jobCategory(jobCategoryResponse)
						.nickname(scrapWriterAccount.getNickname())
						.createdAt(scrap.getCreatedAt().toLocalDate())
						.build());
			}
		);

		return new ReadScrapPageResponse(scrapPageDto.getTotalPages(), scrapPageDto.getTotalElements(),
			readScrapPageResponseScraps);
	}

	private void checkScrapAuthority(long scrapWriterAccountId, long requestingAccountId) {
		if (scrapWriterAccountId != requestingAccountId) {
			log.info("Bad Request");
			throw new IbBadRequestException("Bad Request");
		}
	}

	private CreateScrapResponse makeCreateScrapResponse(Interview interview,
		ScrapWithScrapQuestionAndScrapAnswerList scrapWithScrapQuestionAndScrapAnswerList) {
		OriginalInterviewResponse originalInterviewResponse = new OriginalInterviewResponse(
			interview.getInterviewId(), interview.getTitle());

		ScrapResponse scrapResponse = scrapMapper.scrapToScrapResponse(
			scrapWithScrapQuestionAndScrapAnswerList.getScrap());

		List<ScrapQuestionAndScrapAnswerResponse> scrapQuestionAndScrapAnswerResponseList = new ArrayList<>();
		scrapWithScrapQuestionAndScrapAnswerList.getScrapQuestionAndScrapAnswerList().forEach(
			scrapQuestionAndScrapAnswer -> scrapQuestionAndScrapAnswerResponseList.add(
				new ScrapQuestionAndScrapAnswerResponse(
					scrapMapper.scrapQuestionToScrapQuestionResponse(scrapQuestionAndScrapAnswer.getScrapQuestion()),
					scrapMapper.scrapAnswerToScrapAnswerResponse(scrapQuestionAndScrapAnswer.getScrapAnswer()))));
		return new CreateScrapResponse(originalInterviewResponse, scrapResponse,
			scrapQuestionAndScrapAnswerResponseList);
	}

	private ReadScrapDetailResponse makeReadScrapResponse(Scrap scrap, Interview interview,
		List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers) {
		ScrapResponse scrapResponse = scrapMapper.scrapToScrapResponse(scrap);
		OriginalInterviewResponse interviewResponse = new OriginalInterviewResponse(
			interview.getInterviewId(), interview.getTitle());

		List<ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersResponses = new ArrayList<>();
		scrapQuestionsWithScrapAnswers.forEach(
			scrapQuestionWithScrapAnswers -> scrapQuestionWithScrapAnswersResponses.add(
				scrapMapper.scrapQuestionWithScrapAnswersToScrapQuestionWithScrapAnswersResponse(
					scrapQuestionWithScrapAnswers))
		);
		return new ReadScrapDetailResponse(scrapResponse, interviewResponse, scrapQuestionWithScrapAnswersResponses);
	}
}
