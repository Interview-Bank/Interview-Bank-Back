package org.hoongoin.interviewbank.scrap.service;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.repository.ScrapQuestionRepository;
import org.hoongoin.interviewbank.scrap.repository.ScrapRepository;
import org.hoongoin.interviewbank.scrap.service.domain.Scrap;
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

	public List<Scrap> findScrapAllByScrapWriterAccoundIdAndPageAndSize(long requestingAccountId, int page, int size) {
		List<ScrapEntity> scrapEntities = scrapRepository.findByAccountIdAndPageableOrderByCreatedAtDesc(
			requestingAccountId, PageRequest.of(page, size));

		List<Scrap> scraps = new ArrayList<>();
		scrapEntities.forEach(scrapEntity -> {
			scraps.add(scrapMapper.scrapEntityToScrap(scrapEntity));
		});
		return scraps;
	}
}
