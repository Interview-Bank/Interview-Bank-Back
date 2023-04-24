package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.ModifyNicknameRequest;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountCommandService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account insertAccount(Account account) {
		Boolean emailExists = accountRepository.existsByEmailAndAccountType(account.getEmail(), AccountType.EMAIL);
		if (Boolean.TRUE.equals(emailExists)) {
			log.info("Email {} Already Exists", account.getEmail());
			throw new IbEntityExistsException("Email " + account.getEmail() + " Already Exists");
		}
		AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
		accountRepository.save(accountEntity);

		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public void resetPassword(long requestingAccountId, String password) {
		AccountEntity accountEntity = accountRepository.findById(requestingAccountId)
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		accountEntity.resetPassword(password);
	}

	@Transactional
	public Account insertIfNotExists(Account account) {
		Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
			account.getEmail(), account.getAccountType());
		if (optionalAccountEntity.isEmpty()) {
			AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
			accountRepository.save(accountEntity);
			account = accountMapper.accountEntityToAccount(accountEntity);
		} else {
			account = accountMapper.accountEntityToAccount(optionalAccountEntity.get());
		}
		return account;
	}

	public Account modifyNickname(ModifyNicknameRequest modifyNicknameRequest, long requestingAccountId) {
		AccountEntity accountEntity = accountRepository.findById(requestingAccountId)
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});
		accountEntity.editNickname(modifyNicknameRequest.getNickname());
		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account updateImageUrl(long requestedAccountId, String uploadedUrl) {
		AccountEntity accountEntity = accountRepository.findById(requestedAccountId)
			.orElseThrow(() -> {
				log.info("Account Not Found");
				return new IbEntityNotFoundException("Account Not Found");
			});

		accountEntity.uploadImageUrl(uploadedUrl);

		return accountMapper.accountEntityToAccount(accountEntity);
	}
}
