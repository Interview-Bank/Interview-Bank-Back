package org.hoongoin.interviewbank.scrap.domain;

import java.util.Optional;

import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapLikeEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapLikeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapLikeQueryService {

	private final ScrapLikeRepository scrapLikeRepository;

	public Optional<ScrapLikeEntity> findByAccountIdAndScrapId(long accountId, long interviewId) {
		return scrapLikeRepository.findByAccountIdAndInterviewId(accountId, interviewId);
	}
}
