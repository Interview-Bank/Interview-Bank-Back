package org.hoongoin.interviewbank.account.application.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {

	GOOGLE("google"),
	NAVER("naver"),
	KAKAO("kakao"),
	EMAIL("email");

	private final String accountType;
}
