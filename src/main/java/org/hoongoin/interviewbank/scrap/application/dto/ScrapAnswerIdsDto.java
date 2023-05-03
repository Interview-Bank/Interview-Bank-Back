package org.hoongoin.interviewbank.scrap.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScrapAnswerIdsDto {

	private long scrapId;
	private long scrapQuestionId;
	private long scrapAnswerId;
}
