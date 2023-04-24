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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapAnswerQueryService {

	private final ScrapAnswerRepository scrapAnswerRepository;

	public void deleteScrapAnswer(ScrapAnswerIdsDto scrapAnswerIdsDto) {
		ScrapAnswerEntity scrapAnswerEntity = scrapAnswerRepository.findById(scrapAnswerIdsDto.getScrapAnswerId())
			.orElseThrow(() -> {
				log.info("Scrap Answer Not Found");
				return new IbEntityNotFoundException("Scrap Answer Not Found");
			});

		ScrapQuestionEntity scrapQuestionEntity = scrapAnswerEntity.getScrapQuestionEntity();
		ScrapEntity scrapEntity = scrapQuestionEntity.getScrapEntity();

		if (isScrapAnswerBelongToScrap(scrapEntity, scrapQuestionEntity, scrapAnswerIdsDto)) {
			log.info("ScrapAnswer Doesn't belong to Scrap");
			throw new IbBadRequestException("Bad Request");
		}

		scrapAnswerRepository.deleteById(scrapAnswerIdsDto.getScrapAnswerId());
	}

	private boolean isScrapAnswerBelongToScrap(ScrapEntity scrapEntity, ScrapQuestionEntity scrapQuestionEntity,
		ScrapAnswerIdsDto scrapAnswerIdsDto) {
		return scrapQuestionEntity.getId() != scrapAnswerIdsDto.getScrapQuestionId()
			|| scrapEntity.getId() != scrapAnswerIdsDto.getScrapId();
	}
}
