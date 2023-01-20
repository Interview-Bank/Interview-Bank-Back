package org.hoongoin.interviewbank.interview.service.domain;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.account.service.domain.Account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Interview {

	public Interview(long interviewId, String title,
		Account account, LocalDateTime createdAt, LocalDateTime updatedAt,
		LocalDateTime deletedAt, boolean deletedFlag) {
		this.interviewId = interviewId;
		this.title = title;
		this.account = account;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.deletedFlag = deletedFlag;
	}

	private long interviewId;
	private String title;
	private Account account;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private boolean deletedFlag;
}
