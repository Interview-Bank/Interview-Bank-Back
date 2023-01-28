package org.hoongoin.interviewbank.scrap.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScrapQuestion {

	private long scrapQuestionId;
	private long scrapId;
	private String content;
}
