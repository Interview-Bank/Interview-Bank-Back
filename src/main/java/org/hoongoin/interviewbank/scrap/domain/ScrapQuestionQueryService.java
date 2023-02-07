package org.hoongoin.interviewbank.scrap.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapQuestionWithScrapAnswers;
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
