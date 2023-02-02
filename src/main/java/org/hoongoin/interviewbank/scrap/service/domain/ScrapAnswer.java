package org.hoongoin.interviewbank.scrap.service.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScrapAnswer {

	public ScrapAnswer(String content){
		this.content = content;
	}

	private long scrapAnswerId;
	private long scrapQuestionId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
