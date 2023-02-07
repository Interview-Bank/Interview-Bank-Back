package org.hoongoin.interviewbank.scrap.domain;

import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapQuestionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapQuestionCommandService {

	private final ScrapQuestionRepository scrapQuestionRepository;

	public void deleteAllScrapQuestionByScrapId(long scrapId) {
		scrapQuestionRepository.deleteAllByScrapId(scrapId);
	}
}
