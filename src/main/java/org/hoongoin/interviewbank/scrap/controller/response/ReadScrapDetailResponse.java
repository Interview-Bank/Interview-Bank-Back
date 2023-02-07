package org.hoongoin.interviewbank.scrap.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadScrapDetailResponse {

	private ScrapResponse scrap;
	private OriginalInterviewResponse originalInterview;
	private List<ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersList;
}
