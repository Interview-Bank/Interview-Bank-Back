package org.hoongoin.interviewbank.scrap.domain;

import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapAnswerEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapAnswerRepository;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapAnswer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapAnswerCommandService {

	private final ScrapAnswerRepository scrapAnswerRepository;
	private final ScrapMapper scrapMapper;

	public ScrapAnswer updateScrapAnswer(ScrapAnswer scrapAnswer) {
		ScrapAnswerEntity scrapAnswerEntity = scrapAnswerRepository.findById(scrapAnswer.getScrapAnswerId())
			.orElseThrow(() -> {
				log.info("Scrap Answer Not Found");
				return new IbEntityNotFoundException("Scrap Answer Not Found");
			});
		ScrapQuestionEntity scrapQuestionEntity = scrapAnswerEntity.getScrapQuestionEntity();
		ScrapEntity scrapEntity = scrapQuestionEntity.getScrapEntity();

		if (scrapQuestionEntity.getId() != scrapAnswer.getScrapQuestionId()
			|| scrapEntity.getId() != scrapAnswer.getScrapId()) {
			log.info("ScrapAnswer Doesn't belong to Scrap");
			throw new IbBadRequestException("Bad Request");
		}

		scrapAnswerEntity.modifyEntity(scrapAnswer.getContent());
		return scrapMapper.scrapAnswerEntityToScrapAnswer(scrapAnswerEntity);
	}

	public void deleteAllScrapAnswerByScrapId(long scrapId) {
		scrapAnswerRepository.deleteAllByScrapId(scrapId);
	}
}
