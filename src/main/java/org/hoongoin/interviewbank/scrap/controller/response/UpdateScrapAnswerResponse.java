package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateScrapAnswerResponse {

	private long scrapAnswerId;
	private long scrapQuestionId;
	private String content;
}
