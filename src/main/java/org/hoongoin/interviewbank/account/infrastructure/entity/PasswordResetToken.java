package org.hoongoin.interviewbank.account.infrastructure.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "PasswordResetToken", timeToLive = 10800)
public class PasswordResetToken {

	private long accountId;
	private String email;
	@Id
	@Indexed
	private String token;
}
