package org.hoongoin.interviewbank.account.application.entity;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountType;
import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

	@Builder
	public Account(long accountId, String nickname, String email, String password, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag, AccountType accountType) {
		this.setAccountId(accountId);
		this.setNickname(nickname);
		this.setEmail(email);
		this.setPassword(password);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setDeletedAt(deletedAt);
		this.setDeletedFlag(deletedFlag);
		this.setAccountType(accountType);
	}

	private Long accountId;
	private String nickname;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
	private AccountType accountType;

	public void setNickname(String nickname) {
		if (nickname == null) {
			throw new IbValidationException("nickname null");
		}
		if (nickname.length() < 1) {
			throw new IbValidationException("nickname length less than 1");
		}
		if (nickname.length() > 16) {
			throw new IbValidationException("nickname length larger than 16");
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
