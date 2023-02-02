package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateScrapAnswerResponse {

	private long scrapQuestionId;
	private long scrapAnswerId;
	private String content;
}
