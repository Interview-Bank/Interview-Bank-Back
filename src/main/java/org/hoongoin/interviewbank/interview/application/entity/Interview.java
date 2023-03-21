package org.hoongoin.interviewbank.interview.application.entity;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Interview {

	@Builder
	public Interview(Long interviewId, String title, Long accountId, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag, String primaryJobCategory,
		String secondaryJobCategory) {
		this.setInterviewId(interviewId);
		this.setTitle(title);
		this.setAccountId(accountId);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setDeletedAt(deletedAt);
		this.setDeletedFlag(deletedFlag);
		this.setPrimaryJobCategory(primaryJobCategory);
		this.setSecondaryJobCategory(secondaryJobCategory);
	}

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
	private String primaryJobCategory;
	private String secondaryJobCategory;

	public void setTitle(String title) {
		if (validateTitle(title)) {
			throw new IbValidationException("title");
		}
		this.title = title;
	}

	private boolean validateTitle(String title) {
		return title.getBytes().length > 128 || title.getBytes().length < 1 || title.isEmpty();
	}
}
