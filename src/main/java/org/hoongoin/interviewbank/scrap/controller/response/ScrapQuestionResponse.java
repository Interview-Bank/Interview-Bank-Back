package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScrapQuestionResponse {

	private long scrapQuestionId;
	private String content;
}
