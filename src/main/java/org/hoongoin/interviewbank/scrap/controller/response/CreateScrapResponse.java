package org.hoongoin.interviewbank.scrap.controller.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateScrapResponse {

	private OriginalInterviewResponse originalInterviewResponse;
	private ScrapResponse scrapResponse;
	private List<ScrapQuestionResponse> scrapQuestionResponseList;
}
