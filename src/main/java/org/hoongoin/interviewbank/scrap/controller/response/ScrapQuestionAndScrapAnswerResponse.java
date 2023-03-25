package org.hoongoin.interviewbank.scrap.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScrapQuestionAndScrapAnswerResponse {

	private ScrapQuestionResponse scrapQuestion;
	private ScrapAnswerResponse scrapAnswer;
}
