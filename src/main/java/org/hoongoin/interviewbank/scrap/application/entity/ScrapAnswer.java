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
public class ScrapAnswer {

	public ScrapAnswer(String content) {
		this.setContent(content);
	}

	@Builder
	public ScrapAnswer(long scrapAnswerId, long scrapQuestionId, long scrapId, String content, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.setScrapAnswerId(scrapAnswerId);
		this.setScrapQuestionId(scrapQuestionId);
		this.setScrapId(scrapId);
		this.setContent(content);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private long scrapAnswerId;
	private long scrapQuestionId;
	private long scrapId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private void setContent(String content) {
		if (content!=null && content.getBytes(StandardCharsets.UTF_8).length > 100000) {
			log.info("Scrap Answer content over 100000 byte");
			throw new IbValidationException("Scrap Answer content over 100000 byte");
		}
		this.content = content;
	}
}
