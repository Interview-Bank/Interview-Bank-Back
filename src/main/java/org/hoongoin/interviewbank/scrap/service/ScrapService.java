package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.service.AccountQueryService;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.interview.service.InterviewQueryService;
import org.hoongoin.interviewbank.interview.service.QuestionQueryService;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.OriginalInterviewResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapDetailResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ReadScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionWithScrapAnswersResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapResponse;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapAndScrapQuestions;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestion;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestionWithScrapAnswers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

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

	@Transactional
	public CreateScrapResponse createScrapByCreateRequest(CreateScrapRequest createScrapRequest,
		String requestingAccountOfEmail) {
		Interview originalInterview = interviewQueryService.findInterviewById(createScrapRequest.getInterviewId());
		List<Question> originalQuestionsInInterview = questionQueryService.findQuestionsByInterviewId(
			createScrapRequest.getInterviewId());

		Scrap scrap = Scrap.builder()
			.interviewId(originalInterview.getInterviewId())
			.title(originalInterview.getTitle())
			.build();
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		List<ScrapQuestion> scrapQuestions = new ArrayList<>();
		for (Question question : originalQuestionsInInterview) {
			scrapQuestions.add(
				ScrapQuestion.builder().scrapId(scrap.getScrapId()).content(question.getContent()).build());
		}
		if (scrapCommandService.existsScrapByOriginalInterviewAndRequestingAccount(originalInterview,
			requestingAccount)) {
			throw new IbEntityExistsException("Scrap");
		}
		ScrapAndScrapQuestions savedScrapAndScrapQuestions = scrapCommandService.insertScrapAndScrapQuestions(scrap,
			scrapQuestions, requestingAccount, originalInterview);

		return makeCreateScrapResponse(originalInterview, savedScrapAndScrapQuestions);
	}

	@Transactional
	public UpdateScrapResponse updateScrapByRequestAndScrapId(UpdateScrapRequest updateScrapRequest, long scrapId,
		String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccount.getAccountId());

		Scrap scrapToUpdate = scrapMapper.updateScrapRequestToScrap(updateScrapRequest);
		scrap = scrapCommandService.updateScrap(scrapId, scrapToUpdate);
		return scrapMapper.scrapToUpdateScrapResponse(scrap);
	}

	@Transactional
	public void deleteScrapByRequestAndScrapId(long scrapId, String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccount.getAccountId());

		scrapQuestionCommandService.deleteAllScrapQuestionByScrapId(scrapId);
		scrapCommandService.deleteScrapById(scrapId);
	}

	@Transactional(readOnly = true)
	public ReadScrapDetailResponse readScrapDetailById(long scrapId, String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		checkScrapAuthority(scrap.getAccountId(), requestingAccount.getAccountId());

		Interview interview = interviewQueryService.findInterviewById(scrap.getInterviewId());

		List<ScrapQuestionWithScrapAnswers> scrapQuestionsWithScrapAnswers = scrapQuestionQueryService
			.findAllScrapQuestionWithScrapAnswersByScrapId(scrapId);

		return makeReadScrapResponse(scrap, interview, scrapQuestionsWithScrapAnswers);
	}

	public List<ReadScrapResponse> readScrapAll(long requestingAccountId, int page, int size) {
		List<Scrap> scraps = scrapQueryService.findScrapAllByScrapWriterAccoundIdAndPageAndSize(requestingAccountId,
			page, size);

		List<ReadScrapResponse> readScrapResponses = new ArrayList<>();
		scraps.forEach(scrap ->
			{readScrapResponses.add(scrapMapper.scrapToReadScrapResponse(scrap));}
		);
		return readScrapResponses;
	}

	private void checkScrapAuthority(long scrapWriterAccountId, long requestingAccountId) {
		if (scrapWriterAccountId != requestingAccountId) {
			throw new IbUnauthorizedException("Scrap");
		}
	}

	private CreateScrapResponse makeCreateScrapResponse(Interview interview,
		ScrapAndScrapQuestions scrapAndScrapQuestions) {
		OriginalInterviewResponse originalInterviewResponse = new OriginalInterviewResponse(
			interview.getInterviewId(), interview.getTitle());
		ScrapResponse scrapResponse = scrapMapper.scrapToScrapResponse(scrapAndScrapQuestions.getScrap());
		List<ScrapQuestionResponse> scrapQuestionResponseList = new ArrayList<>();
		scrapAndScrapQuestions.getScrapQuestionList().forEach(
			scrapQuestion -> scrapQuestionResponseList.add(
				scrapMapper.scrapQuestionToScrapQuestionResponse(scrapQuestion)));
		return new CreateScrapResponse(originalInterviewResponse, scrapResponse, scrapQuestionResponseList);
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
