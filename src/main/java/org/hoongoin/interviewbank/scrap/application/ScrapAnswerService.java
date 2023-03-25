package org.hoongoin.interviewbank.scrap.application;

import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.exception.IbAccountNotMatchException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.controller.request.UpdateScrapAnswerRequest;
import org.hoongoin.interviewbank.scrap.controller.response.UpdateScrapAnswerResponse;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapAnswer;
import org.hoongoin.interviewbank.scrap.application.dto.ScrapAnswerIdsDto;
import org.hoongoin.interviewbank.scrap.domain.ScrapAnswerCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapAnswerQueryService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQueryService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQuestionQueryService;
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
	private final ScrapMapper scrapMapper;

	@Transactional
	public UpdateScrapAnswerResponse updateScrapAnswerByRequestAndSIds(
		UpdateScrapAnswerRequest updateScrapAnswerRequest, ScrapAnswerIdsDto scrapAnswerIdsDto,
		long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapAnswerIdsDto.getScrapId());
		checkScrapAuthority(scrap.getAccountId(), requestingAccountId);

		ScrapAnswer updatedScrapAnswer = scrapAnswerCommandService.updateScrapAnswer(
			ScrapAnswer.builder()
				.scrapAnswerId(scrapAnswerIdsDto.getScrapAnswerId())
				.scrapQuestionId(scrapAnswerIdsDto.getScrapQuestionId())
				.scrapId(scrapAnswerIdsDto.getScrapId())
				.content(updateScrapAnswerRequest.getContent())
				.build()
		);
		return scrapMapper.scrapAnswerToUpdateScrapAnswerResponse(updatedScrapAnswer);
	}

	public void deleteScrapAnswerByIds(ScrapAnswerIdsDto scrapAnswerIdsDto, long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapAnswerIdsDto.getScrapId());
		checkScrapAuthority(scrap.getAccountId(), requestingAccountId);

		scrapAnswerQueryService.deleteScrapAnswer(scrapAnswerIdsDto);
	}

	private void checkScrapAuthority(long scrapWriterAccountId, long requestingAccountId) {
		if (scrapWriterAccountId != requestingAccountId) {
			throw new IbAccountNotMatchException("Scrap");
		}
	}
}
