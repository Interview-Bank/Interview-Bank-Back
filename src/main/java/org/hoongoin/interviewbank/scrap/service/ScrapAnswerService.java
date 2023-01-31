package org.hoongoin.interviewbank.scrap.service;

import org.hoongoin.interviewbank.account.service.AccountQueryService;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapAnswerRequest;
import org.hoongoin.interviewbank.scrap.controller.response.CreateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapAnswer;
import org.hoongoin.interviewbank.scrap.service.dto.ScrapAnswerIdsDto;
import org.hoongoin.interviewbank.scrap.service.dto.ScrapQuestionIdsDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapAnswerService {

	private final ScrapQueryService scrapQueryService;
	private final ScrapQuestionQueryService scrapQuestionQueryService;
	private final ScrapAnswerCommandService scrapAnswerCommandService;
	private final ScrapAnswerQueryService scrapAnswerQueryService;
	private final AccountQueryService accountQueryService;
	private final ScrapMapper scrapMapper;

	public CreateScrapAnswerResponse createScrapAnswerByScrapIdAndScrapQuestionId(
		ScrapQuestionIdsDto scrapQuestionIdsDto, String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapQuestionIdsDto.getScrapId());
		if (scrap.getAccountId() != requestingAccount.getAccountId()) {
			throw new IbUnauthorizedException("Scrap");
		}

		if (!scrapQuestionQueryService.existsScrapQuestionByScrapQuestionId(scrapQuestionIdsDto.getScrapQuestionId())) {
			throw new IbEntityNotFoundException("ScrapQuestion");
		}
		ScrapAnswer scrapAnswer = scrapAnswerCommandService.createScrapAnswerByScrapQuestionId(
			scrapQuestionIdsDto.getScrapQuestionId(),
			ScrapAnswer.builder().scrapQuestionId(scrapQuestionIdsDto.getScrapQuestionId()).build());
		return scrapMapper.scrapAnswerToCreateScrapAnswerResponse(scrapAnswer);
	}

	@Transactional
	public UpdateScrapAnswerResponse updateScrapAnswerByRequestAndSIds(
		UpdateScrapAnswerRequest updateScrapAnswerRequest, ScrapAnswerIdsDto scrapAnswerIdsDto,
		String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapAnswerIdsDto.getScrapId());
		if (scrap.getAccountId() != requestingAccount.getAccountId()) {
			throw new IbUnauthorizedException("Scrap");
		}

		ScrapAnswer updatedScrapAnswer = scrapAnswerCommandService.updateScrapAnswer(scrapAnswerIdsDto,
			new ScrapAnswer(updateScrapAnswerRequest.getContent()));
		return scrapMapper.scrapAnswerToUpdateScrapAnswerResponse(updatedScrapAnswer);
	}

	public void deleteScrapAnswerByIds(ScrapAnswerIdsDto scrapAnswerIdsDto, String requestingAccountOfEmail) {
		Account requestingAccount = accountQueryService.findAccountByEmail(requestingAccountOfEmail);
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapAnswerIdsDto.getScrapId());
		if (scrap.getAccountId() != requestingAccount.getAccountId()) {
			throw new IbUnauthorizedException("Scrap");
		}

		scrapAnswerQueryService.deleteScrapAnswer(scrapAnswerIdsDto);
	}
}
