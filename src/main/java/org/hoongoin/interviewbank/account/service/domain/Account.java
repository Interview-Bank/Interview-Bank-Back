package org.hoongoin.interviewbank.account.service.domain;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

	public Account(long accountId, String nickname, String email, String password, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, boolean deletedFlag) {
		this.accountId = accountId;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		this.deletedFlag = deletedFlag;
	}

	private long accountId;
	private String nickname;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private boolean deletedFlag;
}
