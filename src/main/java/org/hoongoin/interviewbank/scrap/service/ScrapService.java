package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.account.service.AccountQueryService;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.interview.service.InterviewQueryService;
import org.hoongoin.interviewbank.interview.service.QuestionQueryService;
import org.hoongoin.interviewbank.interview.service.domain.Interview;
import org.hoongoin.interviewbank.interview.service.domain.Question;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.controller.request.CreateScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapResponse;
import org.hoongoin.interviewbank.scrap.controller.response.OriginalInterviewResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapQuestionResponse;
import org.hoongoin.interviewbank.scrap.controller.response.ScrapResponse;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapAndScrapQuestions;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapService {

	private final ScrapCommandService scrapCommandService;
	private final AccountQueryService accountQueryService;
	private final InterviewQueryService interviewQueryService;
	private final QuestionQueryService questionQueryService;
	private final ScrapMapper scrapMapper;

	@Transactional
	public CreateScrapResponse createScrapByCreateRequest(CreateScrapRequest createScrapRequest,
		String requestingAccountOfEmail) {
		Interview originalInterview = interviewQueryService.findEntityById(createScrapRequest.getInterviewId());
		Account interviewWriterAccount = accountQueryService.findAccountById(originalInterview.getAccountId());
		List<Question> originalQuestionsInInterview = questionQueryService.findEntitiesByInterviewId(createScrapRequest.getInterviewId());

		Scrap scrap = Scrap.builder().interviewId(originalInterview.getInterviewId()).title(originalInterview.getTitle()).build();
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		List<ScrapQuestion> scrapQuestions = new ArrayList<>();
		for(Question question: originalQuestionsInInterview){
			scrapQuestions.add(ScrapQuestion.builder().scrapId(scrap.getScrapId()).content(question.getContent()).build());
		}
		if(scrapCommandService.existsScrapByOriginalInterviewAndRequestingAccount(originalInterview, requestingAccount)){
			throw new IbEntityExistsException("Scrap");
		}
		ScrapAndScrapQuestions savedScrapAndScrapQuestions  = scrapCommandService.insertScrapAndScrapQuestions(scrap,
			scrapQuestions, requestingAccount, originalInterview);

		OriginalInterviewResponse originalInterviewResponse = new OriginalInterviewResponse(
			originalInterview.getInterviewId(), originalInterview.getTitle(), interviewWriterAccount.getNickname());
		ScrapResponse scrapResponse = scrapMapper.scrapToScrapResponse(savedScrapAndScrapQuestions.getScrap());
		List<ScrapQuestionResponse> scrapQuestionResponseList = new ArrayList<>();
		savedScrapAndScrapQuestions.getScrapQuestionList().forEach(
			scrapQuestion -> scrapQuestionResponseList.add(scrapMapper.scrapQuestionToScrapQuestionResponse(scrapQuestion)));

		return new CreateScrapResponse(originalInterviewResponse, scrapResponse, scrapQuestionResponseList);
	}
}
