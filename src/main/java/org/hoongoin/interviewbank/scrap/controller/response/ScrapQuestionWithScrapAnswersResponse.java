package org.hoongoin.interviewbank.scrap.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScrapQuestionWithScrapAnswersResponse {

	private long scrapQuestionId;
	private String content;
	private List<ScrapAnswerResponse> scrapAnswerResponseList;

	@Getter
	@Setter
	@AllArgsConstructor
	public static class ScrapAnswerResponse{
		private long scrapAnswerId;
		private String content;
	}
}
