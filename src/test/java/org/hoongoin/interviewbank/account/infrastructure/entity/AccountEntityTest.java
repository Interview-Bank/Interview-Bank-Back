package org.hoongoin.interviewbank.account.infrastructure.entity;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
class AccountEntityTest {

	@Autowired
	AccountRepository accountRepository;

	@Test
	void AccountEntity_Fail_MultiColumnIndexDuplication() {
		//given
		String testNickname1 = "nickname1";
		String testNickname2 = "nickname2";
		String email = "email@gmail.com";
		String password = "password";
		AccountEntity accountEntity1 = AccountEntity.builder()
			.nickname(testNickname1)
			.email(email)
			.accountType(AccountType.EMAIL)
			.password(password)
			.build();

		accountRepository.saveAndFlush(accountEntity1);

		//when
		AccountEntity accountEntity2 = AccountEntity.builder()
			.nickname(testNickname2)
			.email(email)
			.password(password)
			.accountType(AccountType.EMAIL)
			.build();

		// then
		assertThatThrownBy(() -> accountRepository.saveAndFlush(accountEntity2)).isInstanceOf(
			DataIntegrityViolationException.class);
	}
}
