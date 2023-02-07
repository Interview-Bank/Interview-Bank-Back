package org.hoongoin.interviewbank.scrap.domain;

import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapAnswerEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapAnswerRepository;
import org.hoongoin.interviewbank.scrap.application.dto.ScrapAnswerIdsDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapAnswerQueryService {

	private final ScrapAnswerRepository scrapAnswerRepository;

	public void deleteScrapAnswer(ScrapAnswerIdsDto scrapAnswerIdsDto) {
		ScrapAnswerEntity scrapAnswerEntity = scrapAnswerRepository.findById(scrapAnswerIdsDto.getScrapAnswerId())
			.orElseThrow(() -> new IbEntityNotFoundException("ScrapAnswer"));

		ScrapQuestionEntity scrapQuestionEntity = scrapAnswerEntity.getScrapQuestionEntity();
		ScrapEntity scrapEntity = scrapQuestionEntity.getScrapEntity();

		if (isScrapAnswerBelongToScrap(scrapEntity, scrapQuestionEntity, scrapAnswerIdsDto)) {
			throw new IbBadRequestException("");
		}

		scrapAnswerRepository.deleteById(scrapAnswerIdsDto.getScrapAnswerId());
	}

	private boolean isScrapAnswerBelongToScrap(ScrapEntity scrapEntity, ScrapQuestionEntity scrapQuestionEntity,
		ScrapAnswerIdsDto scrapAnswerIdsDto) {
		return scrapQuestionEntity.getId() != scrapAnswerIdsDto.getScrapQuestionId()
			|| scrapEntity.getId() != scrapAnswerIdsDto.getScrapId();
	}
}
