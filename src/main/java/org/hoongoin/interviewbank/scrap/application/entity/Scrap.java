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
public class Scrap {

	@Builder
	public Scrap(long scrapId, long accountId, long interviewId, String title, boolean isPublic, Long jobCategoryId,
		LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.setScrapId(scrapId);
		this.setAccountId(accountId);
		this.setInterviewId(interviewId);
		this.setIsPublic(isPublic);
		this.setTitle(title);
		this.setJobCategoryId(jobCategoryId);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
	}

	private long scrapId;
	private long accountId;
	private long interviewId;
	private String title;
	private boolean isPublic;
	private Long jobCategoryId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public boolean getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setTitle(String title) {
		if (title.getBytes(StandardCharsets.UTF_8).length > 128) {
			log.info("Title over 128 byte");
			throw new IbValidationException("Title over 128 byte");
		}
		this.title = title;
	}
}
