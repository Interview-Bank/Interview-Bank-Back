package org.hoongoin.interviewbank.scrap.service;

import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.entity.ScrapAnswerEntity;
import org.hoongoin.interviewbank.scrap.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.repository.ScrapAnswerRepository;
import org.hoongoin.interviewbank.scrap.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapAnswer;
import org.hoongoin.interviewbank.scrap.service.dto.ScrapAnswerIdsDto;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapAnswerCommandService {

	private final ScrapAnswerRepository scrapAnswerRepository;
	private final ScrapQuestionRepository scrapQuestionRepository;
	private final ScrapMapper scrapMapper;

	public ScrapAnswer createScrapAnswerByScrapQuestionId(long scrapQuestionId, ScrapAnswer scrapAnswer) {
		ScrapQuestionEntity scrapQuestionEntity = scrapQuestionRepository.findById(scrapQuestionId)
			.orElseThrow(() -> new IbEntityNotFoundException("ScrapQuestion"));

		ScrapAnswerEntity scrapAnswerEntity = scrapMapper
			.scrapAnswerAndScrapQuestionEntityToScrapAnswerEntity(scrapAnswer, scrapQuestionEntity);
		scrapAnswerEntity = scrapAnswerRepository.save(scrapAnswerEntity);
		return scrapMapper.scrapAnswerEntityToScrapAnswer(scrapAnswerEntity);
	}

	public ScrapAnswer updateScrapAnswer(ScrapAnswerIdsDto scrapAnswerIdsDto, ScrapAnswer scrapAnswer) {
		ScrapAnswerEntity scrapAnswerEntity = scrapAnswerRepository.findById(scrapAnswerIdsDto.getScrapAnswerId())
			.orElseThrow(() -> new IbEntityNotFoundException("ScrapAnswer"));
		ScrapQuestionEntity scrapQuestionEntity = scrapAnswerEntity.getScrapQuestionEntity();
		ScrapEntity scrapEntity = scrapQuestionEntity.getScrapEntity();

		if (scrapQuestionEntity.getId() != scrapAnswerIdsDto.getScrapQuestionId()
			|| scrapEntity.getId() != scrapAnswerIdsDto.getScrapId()) {
			throw new IbBadRequestException("");
		}

		scrapAnswerEntity.modifyEntity(scrapAnswer.getContent());
		return scrapMapper.scrapAnswerEntityToScrapAnswer(scrapAnswerEntity);
	}
}
