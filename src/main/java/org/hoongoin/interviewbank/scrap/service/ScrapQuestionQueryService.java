package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.hoongoin.interviewbank.scrap.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.service.domain.ScrapQuestion;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapQuestionQueryService {

	private final ScrapQuestionRepository scrapQuestionRepository;
	private final ScrapMapper scrapMapper;

	public List<ScrapQuestion> findAllScrapQuestionByScrapId(long scrapId){
		List<ScrapQuestionEntity> scrapQuestionEntities = scrapQuestionRepository.findAllByScrapId(scrapId);

		List<ScrapQuestion> savedScrapQuestions = new ArrayList<>();
		scrapQuestionEntities.forEach(scrapQuestionEntity -> {
			savedScrapQuestions.add(scrapMapper.scrapQuestionEntityToScrapQuestion(scrapQuestionEntity));
		});
		return savedScrapQuestions;
	}
}
