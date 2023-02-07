package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestionWithScrapAnswers;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapQuestionQueryService {

	private final ScrapQuestionRepository scrapQuestionRepository;
	private final ScrapMapper scrapMapper;

	public List<ScrapQuestionWithScrapAnswers> findAllScrapQuestionWithScrapAnswersByScrapId(long scrapId) {
		List<ScrapQuestionEntity> scrapQuestionEntities = scrapQuestionRepository
			.findAllWithScrapAnswerEntitiesByScrapId(scrapId);

		List<ScrapQuestionWithScrapAnswers> savedScrapQuestions = new ArrayList<>();
		scrapQuestionEntities.forEach(scrapQuestionEntity -> {
			savedScrapQuestions.add(
				scrapMapper.scrapQuestionEntityWithScrapAnswerEntitiesToScrapQuestionWithScrapAnswers(
					scrapQuestionEntity));
		});

		return savedScrapQuestions;
	}

	public boolean existsScrapQuestionByScrapQuestionId(long scrapQuestionId) {
		return scrapQuestionRepository.existsById(scrapQuestionId);
	}
}
