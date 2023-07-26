package org.hoongoin.interviewbank.scrap.application.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScrapLike {

	@Builder
	public ScrapLike(Long scrapLikeId, Long accountId, Long scrapId, Boolean like){
		this.setScrapLikeId(scrapLikeId);
		this.setAccountId(accountId);
		this.setScrapId(scrapId);
		this.setLike(like);
	}

	private Long scrapLikeId;
	private Long accountId;
	private Long scrapId;
	private Boolean like;
}
