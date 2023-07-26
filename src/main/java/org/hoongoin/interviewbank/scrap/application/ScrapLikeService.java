package org.hoongoin.interviewbank.scrap.application;

import java.util.Optional;

import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.application.entity.ScrapLike;
import org.hoongoin.interviewbank.scrap.controller.request.LikeScrapRequest;
import org.hoongoin.interviewbank.scrap.controller.response.LikeScrapResponse;
import org.hoongoin.interviewbank.scrap.domain.ScrapLikeCommandService;
import org.hoongoin.interviewbank.scrap.domain.ScrapLikeQueryService;
import org.hoongoin.interviewbank.scrap.domain.ScrapQueryService;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapLikeEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapLikeService {

	private final ScrapLikeCommandService scrapLikeCommandService;
	private final ScrapLikeQueryService scrapLikeQueryService;
	private final ScrapQueryService scrapQueryService;

	@Transactional
	public LikeScrapResponse likeScrap(long scrapId, LikeScrapRequest likeScrapRequest, long requestingAccountId) {
		Scrap scrap = scrapQueryService.findScrapByScrapId(scrapId);
		if(!scrap.getIsPublic() && scrap.getAccountId()!=requestingAccountId){
			throw new IbBadRequestException("Scrap Private");
		}

		Optional<ScrapLikeEntity> optionalScrapLikeEntity = scrapLikeQueryService.findByAccountIdAndScrapId(
			requestingAccountId, scrapId);

		LikeScrapResponse likeScrapResponse;
		if (optionalScrapLikeEntity.isPresent()) {
			ScrapLikeEntity scrapLikeEntity = optionalScrapLikeEntity.get();
			scrapLikeEntity.modifyLike(likeScrapRequest.getLike());
			likeScrapResponse = new LikeScrapResponse(scrapLikeEntity.isLike());
		} else {
			ScrapLike scrapLike = ScrapLike.builder()
				.scrapId(scrapId)
				.accountId(requestingAccountId)
				.like(likeScrapRequest.getLike())
				.build();
			scrapLike = scrapLikeCommandService.insertScrapLike(scrapLike);
			likeScrapResponse = new LikeScrapResponse(scrapLike.getLike());
		}

		return likeScrapResponse;
	}
}
