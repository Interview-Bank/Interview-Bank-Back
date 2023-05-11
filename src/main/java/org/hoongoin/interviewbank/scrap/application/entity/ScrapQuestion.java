package org.hoongoin.interviewbank.scrap.application.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class ScrapQuestion {

	@Builder
	public ScrapQuestion(long scrapQuestionId, long scrapId, String content, String gptAnswer, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.setScrapQuestionId(scrapQuestionId);
		this.setScrapId(scrapId);
		this.setContent(content);
		this.setGptAnswer(gptAnswer);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private long scrapQuestionId;
	private long scrapId;
	private String content;
	private String gptAnswer;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public void setContent(String content) {
		if (content.getBytes(StandardCharsets.UTF_8).length > 100000) {
			log.info("Scrap Question length over 100000 byte");
			throw new IbValidationException("Scrap Question length over 100000 byte");
		}
		this.content = content;
	}
}
