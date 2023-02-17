package org.hoongoin.interviewbank.scrap.application.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScrapQuestion {

	@Builder
	public ScrapQuestion(long scrapQuestionId, long scrapId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.setScrapQuestionId(scrapQuestionId);
		this.setScrapId(scrapId);
		this.setContent(content);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private long scrapQuestionId;
	private long scrapId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public void setContent(String content) {
		if (content.getBytes(StandardCharsets.UTF_8).length > 100000) {
			throw new IbValidationException("Scrap Question length over 100000 byte");
		}
		this.content = content;
	}
}
