package org.hoongoin.interviewbank.scrap.service.domain;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScrapQuestionWithScrapAnswers {

	private long scrapQuestionId;
	private long scrapId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<ScrapAnswer> scrapAnswers;

	public void addScrapAnswer(ScrapAnswer scrapAnswer){
		this.scrapAnswers.add(scrapAnswer);
	}
}
