package org.hoongoin.interviewbank.scrap.service.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Scrap {

	private long scrapId;
	private long accountId;
	private long interviewId;
	private String title;
}
