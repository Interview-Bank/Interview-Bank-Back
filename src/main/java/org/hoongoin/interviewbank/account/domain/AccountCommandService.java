package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.GoogleUerInfoResponse;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account insertAccount(Account account) {
		Boolean emailExists = accountRepository.existsByEmail(account.getEmail());
		if (Boolean.TRUE.equals(emailExists)) {
			throw new IbEntityExistsException(account.getEmail());
		}
		AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
		accountRepository.save(accountEntity);

		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account resetPassword(long requestingAccountId, String password) {
		AccountEntity accountEntity = accountRepository.findById(requestingAccountId)
			.orElseThrow(() -> new IbEntityNotFoundException("Account"));

		accountEntity.modifyEntity(password);
		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account saveGoogleUserIfNotExists(GoogleUerInfoResponse googleUerInfoResponse) {
		Account account;
		if(!accountRepository.existsByEmailAndAccountType(googleUerInfoResponse.getEmail(), AccountType.GOOGLE)){
			AccountEntity accountEntity = AccountEntity.builder()
					.nickname(googleUerInfoResponse.getName())
					.email(googleUerInfoResponse.getEmail())
					.picture(googleUerInfoResponse.getPicture())
					.accountType(AccountType.GOOGLE)
					.build();
			accountRepository.save(accountEntity);
			account = accountMapper.accountEntityToAccount(accountEntity);
		}
		else{
			account = accountMapper.googleUerInfoResponseToAccount(googleUerInfoResponse);
		}
		return account;
	}
}
