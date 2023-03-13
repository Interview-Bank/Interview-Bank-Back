package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.dto.GoogleUerInfo;
import org.hoongoin.interviewbank.account.application.dto.KakaoUerInfoResponse;
import org.hoongoin.interviewbank.account.application.dto.NaverUserInfoResponse;
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
    public Account saveGoogleUserIfNotExists(GoogleUerInfo googleUerInfo) {
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
                googleUerInfo.getEmail(), AccountType.GOOGLE);
        AccountEntity accountEntity = optionalAccountEntity.get();
        if (optionalAccountEntity.isEmpty()) {
            accountEntity = AccountEntity.builder()
                    .nickname(googleUerInfo.getName())
                    .email(googleUerInfo.getEmail())
                    .picture(googleUerInfo.getPicture())
                    .accountType(AccountType.GOOGLE)
                    .build();
            accountRepository.save(accountEntity);
        }
        return accountMapper.accountEntityToAccount(accountEntity);
    }

    @Transactional
    public Account saveNaverUserInfoIfNotExists(NaverUserInfoResponse naverUserInfoResponse) {
        Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
                naverUserInfoResponse.getEmail(), AccountType.NAVER);
        AccountEntity accountEntity = optionalAccountEntity.get();
        if (optionalAccountEntity.isEmpty()) {
            accountEntity = AccountEntity.builder()
                    .nickname(naverUserInfoResponse.getName())
                    .email(naverUserInfoResponse.getEmail())
                    .picture(naverUserInfoResponse.getProfileImage())
                    .accountType(AccountType.NAVER)
                    .build();
            accountRepository.save(accountEntity);
        }
        return accountMapper.accountEntityToAccount(accountEntity);
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
