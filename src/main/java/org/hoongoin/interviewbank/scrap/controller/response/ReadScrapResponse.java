package org.hoongoin.interviewbank.scrap.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadScrapResponse {

	private ScrapResponse scrap;
	private OriginalInterviewResponse originalInterview;
	private List<ScrapQuestionResponse> scrapQuestionList;
}
