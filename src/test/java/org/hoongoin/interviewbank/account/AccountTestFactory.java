package org.hoongoin.interviewbank.account;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;

public class AccountTestFactory {

	private static final long testAccountId = 1L;
	private static final String testNickname = "hunki";
	private static final String testEmail = "gnsrl76@naver.com";
	private static final String testPassword = "asdfasdf12!";

	public static AccountEntity createAccountEntity() {
		return AccountEntity.builder()
			.id(testAccountId)
			.nickname(testNickname)
			.password(testPassword)
			.email(testEmail)
			.build();
	}

	public static Account createAccount(){
		return Account.builder()
			.accountId(testAccountId)
			.nickname(testNickname)
			.email(testEmail)
			.password(testPassword)
			.build();
	}
}
