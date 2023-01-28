package org.hoongoin.interviewbank.scrap.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateScrapResponse {

	private OriginalInterviewResponse originalInterviewResponse;
	private ScrapResponse scrapResponse;
	private List<ScrapQuestionResponse> scrapQuestionResponseList;
}
