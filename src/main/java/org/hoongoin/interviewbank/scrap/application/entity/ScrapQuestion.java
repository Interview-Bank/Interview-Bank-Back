package org.hoongoin.interviewbank.scrap.application.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ScrapQuestion {

	private long scrapQuestionId;
	private long scrapId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
