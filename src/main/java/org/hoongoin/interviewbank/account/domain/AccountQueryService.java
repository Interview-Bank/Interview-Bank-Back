package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account findAccountByEmail(String email) {
		AccountEntity accountEntity = accountRepository.findByEmail(email)
			.orElseThrow(() -> new IbEntityNotFoundException("Account"));
		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account findAccountByAccountId(long accountId) {
		return accountMapper.accountEntityToAccount(
			accountRepository.findById(accountId).orElseThrow(() -> new IbEntityNotFoundException("Account")));
	}
}