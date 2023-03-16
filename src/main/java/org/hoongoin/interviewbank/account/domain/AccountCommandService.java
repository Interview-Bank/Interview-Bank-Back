package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.dto.KakaoUerInfoResponse;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;

	public Account insertAccount(Account account) {
		Boolean emailExists = accountRepository.existsByEmailAndAccountType(account.getEmail(), AccountType.EMAIL);
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

	@Transactional
	public Account insertIfNotExists(Account account) {
		Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
			account.getEmail(), account.getAccountType());
		if (optionalAccountEntity.isEmpty()) {
			AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
			accountRepository.save(accountEntity);
			account = accountMapper.accountEntityToAccount(accountEntity);
		}
		return account;
	}

	@Transactional
	public Account saveKakaoUserIfNotExists(KakaoUerInfoResponse kakaoUserInfoResponse) {
		Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
			kakaoUserInfoResponse.getKakaoAccount().getEmail(), AccountType.NAVER);
		AccountEntity accountEntity = optionalAccountEntity.get();
		if (optionalAccountEntity.isEmpty()) {
			accountEntity = AccountEntity.builder()
				.nickname(kakaoUserInfoResponse.getKakaoAccount().getProfile().getNickname())
				.email(kakaoUserInfoResponse.getKakaoAccount().getEmail())
				.accountType(AccountType.KAKAO)
				.build();
			accountRepository.save(accountEntity);
		}
		return accountMapper.accountEntityToAccount(accountEntity);
	}
}
