package org.hoongoin.interviewbank.scrap.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.common.dto.PageDto;
import org.hoongoin.interviewbank.scrap.ScrapMapper;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.repository.ScrapRepository;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ScrapQueryService {

	private final ScrapRepository scrapRepository;
	private final ScrapMapper scrapMapper;

	public Scrap findScrapByScrapId(long scrapId) {
		ScrapEntity scrapEntity = scrapRepository.findById(scrapId)
			.orElseThrow(() -> {
				log.info("Scrap Not Found");
				return new IbEntityNotFoundException("Scrap Not Found");
			});
		return scrapMapper.scrapEntityToScrap(scrapEntity);
	}

	public PageDto<Scrap> findScrapAllByScrapWriterAccountIdAndPageAndSize(long requestingAccountId, int page,
		int size) {
		Page<ScrapEntity> scrapEntities = scrapRepository.findByAccountIdAndPageableOrderByCreatedAtDesc(
			requestingAccountId, PageRequest.of(page, size));

		PageDto<Scrap> scrapPageDto = new PageDto<>();
		scrapPageDto.setTotalPages(scrapEntities.getTotalPages());
		scrapPageDto.setTotalElements(scrapEntities.getTotalElements());

		List<Scrap> scraps = new ArrayList<>();
		scrapEntities.forEach(scrapEntity -> scraps.add(scrapMapper.scrapEntityToScrap(scrapEntity)));
		scrapPageDto.setContent(scraps);
		return scrapPageDto;
	}
}
