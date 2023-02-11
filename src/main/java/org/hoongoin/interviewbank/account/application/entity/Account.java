package org.hoongoin.interviewbank.account.application.entity;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

	@Builder
	public Account(long accountId, String nickname, String email, String password, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag) {
		this.setAccountId(accountId);
		this.setNickname(nickname);
		this.setEmail(email);
		this.setPassword(password);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setDeletedAt(deletedAt);
		this.setDeletedFlag(deletedFlag);
	}

	private Long accountId;
	private String nickname;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;

	public void setNickname(String nickname) {
		if (nickname == null) {
			throw new IbValidationException("nickname null");
		}
		if (nickname.getBytes(StandardCharsets.UTF_8).length < 4) {
			throw new IbValidationException("nickname less than 4 bytes");
		}
		if (nickname.getBytes(StandardCharsets.UTF_8).length > 30) {
			throw new IbValidationException("nickname larger than 30 bytes");
		}
		this.nickname = nickname;
	}

	public void setEmail(String email) {
		if (email == null) {
			throw new IbValidationException("email null");
		}
		if (email.length() > 320) {
			throw new IbValidationException("email larger than 320 length");
		}
		this.email = email;
	}

	public void setPassword(String password) {
		if (password == null) {
			throw new IbValidationException("password null");
		}
		this.password = password;
	}
}