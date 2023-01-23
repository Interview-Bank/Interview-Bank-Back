package org.hoongoin.interviewbank.account.service;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.repository.AccountRepository;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.CustomEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account findEntityByEmail(String email) {
		AccountEntity accountEntity = accountRepository.findByEmail(email)
			.orElseThrow(() -> new CustomEntityNotFoundException("Account"));
		return accountMapper.accountEntityToAccount(accountEntity);
	}
}