package org.hoongoin.interviewbank.scrap.application.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scrap {

	@Builder
	public Scrap(long scrapId, long accountId, long interviewId, String title, Long jobCategoryId,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.setScrapId(scrapId);
		this.setAccountId(accountId);
		this.setInterviewId(interviewId);
		this.setTitle(title);
		this.setJobCategoryId(jobCategoryId);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private long scrapId;
	private long accountId;
	private long interviewId;
	private String title;
	private Long jobCategoryId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public void setTitle(String title) {
		if (title.getBytes(StandardCharsets.UTF_8).length > 128) {
			throw new IbValidationException("Title over 128 byte");
		}
		this.title = title;
	}
}
