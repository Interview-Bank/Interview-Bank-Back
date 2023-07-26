package org.hoongoin.interviewbank.scrap.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapLike;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapLikeEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapLikeRepository;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapLikeCommandService {

	private final ScrapLikeRepository scrapLikeRepository;
	private final ScrapRepository scrapRepository;
	private final AccountRepository accountRepository;
	private final ScrapMapper scrapMapper;

	public ScrapLike insertScrapLike(ScrapLike scrapLike) {

		ScrapEntity scrapEntity = scrapRepository.findById(scrapLike.getScrapId())
			.orElseThrow(() -> {
				log.info("ScrapEntity Not Found");
				throw new IbEntityNotFoundException("ScrapEntity Not Found");
			});

		AccountEntity accountEntity = accountRepository.findById(scrapLike.getAccountId())
			.orElseThrow(() -> {
				log.info("AccountEntity Not Found");
				throw new IbEntityNotFoundException("AccountEntity Not Found");
			});

		ScrapLikeEntity scrapLikeEntity = ScrapLikeEntity.builder()
			.scrapEntity(scrapEntity)
			.accountEntity(accountEntity)
			.like(scrapLike.getLike())
			.build();
		scrapLikeRepository.save(scrapLikeEntity);

		return scrapMapper.scrapLikeEntityToScrapLike(scrapLikeEntity);
	}
}
