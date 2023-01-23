package org.hoongoin.interviewbank.account.service.domain;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {

	public Account(long accountId, String nickname, String email, String password, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, boolean deletedFlag) {
		this.accountId = accountId;
		this.nickname = nickname;
		this.setNickname(nickname);
		this.setEmail(email);
		this.setPassword(password);
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

	public void setNickname(String nickname){
		if(nickname==null){
			throw new IbValidationException("nickname null");
		}
		if(nickname.getBytes(StandardCharsets.UTF_8).length<4){
			throw new IbValidationException("nickname less than 4 bytes");
		}
		if(nickname.getBytes(StandardCharsets.UTF_8).length>30){
			throw new IbValidationException("nickname larger than 30 bytes");
		}
		this.nickname = nickname;
	}

	public void setEmail(String email){
		if(email==null){
			throw new IbValidationException("email null");
		}
		if(email.length()>320){
			throw new IbValidationException("email larger than 320 length");
		}
		this.email = email;
	}

	public void setPassword(String password){
		if(password==null){
			throw new IbValidationException("password null");
		}
		this.password = password;
	}
}
