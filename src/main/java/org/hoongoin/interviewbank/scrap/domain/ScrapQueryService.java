package org.hoongoin.interviewbank.scrap.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapRepository;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapQueryService {

	private final ScrapRepository scrapRepository;
	private final ScrapMapper scrapMapper;

	public Scrap findScrapByScrapId(long scrapId) {
		ScrapEntity scrapEntity = scrapRepository.findById(scrapId)
			.orElseThrow(() -> new IbEntityNotFoundException("Scrap"));
		return scrapMapper.scrapEntityToScrap(scrapEntity);
	}

	public List<Scrap> findScrapAllByScrapWriterAccountIdAndPageAndSize(long requestingAccountId, int page, int size) {
		List<ScrapEntity> scrapEntities = scrapRepository.findByAccountIdAndPageableOrderByCreatedAtDesc(
			requestingAccountId, PageRequest.of(page, size));

		List<Scrap> scraps = new ArrayList<>();
		scrapEntities.forEach(scrapEntity -> {
			scraps.add(scrapMapper.scrapEntityToScrap(scrapEntity));
		});
		return scraps;
	}
}
