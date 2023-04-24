package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbSoftDeleteException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountQueryService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account findAccountByEmailAndAccountType(String email, AccountType accountType) throws
		IbEntityNotFoundException, IbSoftDeleteException {
		AccountEntity accountEntity = accountRepository.findByEmailAndAccountType(email, accountType)
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		isDeleted(accountEntity);

		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account findAccountByAccountId(long accountId) {
		AccountEntity accountEntity = accountRepository.findById(accountId)
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		isDeleted(accountEntity);

		return accountMapper.accountEntityToAccount(accountEntity);
	}

	private void isDeleted(AccountEntity accountEntity) {
		if (Boolean.TRUE.equals(accountEntity.getDeletedFlag())) {
			throw new IbSoftDeleteException("Account");
		}
	}
}
