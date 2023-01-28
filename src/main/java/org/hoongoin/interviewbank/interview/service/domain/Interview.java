package org.hoongoin.interviewbank.interview.service.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Interview {

	public Interview(String title, Long accountId) {
		this(null, title, accountId);
	}

	public Interview(Long interviewId, String title, Long accountId) {
		this(interviewId, title, accountId, null, null, null, null);
	}

	public Interview(Long interviewId, String title,
		Long accountId, LocalDateTime createdAt, LocalDateTime updatedAt,
		LocalDateTime deletedAt, Boolean deletedFlag) {
		this(interviewId, title, accountId, createdAt, updatedAt, deletedAt, deletedFlag, "");
	}

	public Interview(Long interviewId, String title, Long accountId, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag, String nickname) {
		this.interviewId = interviewId;
		this.title = title;
		this.accountId = accountId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.deletedFlag = deletedFlag;
		this.nickname = nickname;
	}

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
	private String nickname;
}