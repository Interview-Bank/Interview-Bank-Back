package org.hoongoin.interviewbank.account;

import org.hoongoin.interviewbank.account.application.dto.GoogleUerInfo;
import org.hoongoin.interviewbank.account.application.dto.KakaoUerInfoResponse;
import org.hoongoin.interviewbank.account.application.dto.NaverUserInfoResponse;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.LoginResponse;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    default Account accountEntityToAccount(AccountEntity accountEntity) {
        return new Account(accountEntity.getId(), accountEntity.getNickname(), accountEntity.getEmail(),
                accountEntity.getPassword(), accountEntity.getCreatedAt(), accountEntity.getUpdatedAt(),
                accountEntity.getDeletedAt(), accountEntity.getDeletedFlag());
    }

    @Mapping(target = "id", source = "accountId")
    AccountEntity accountToAccountEntity(Account account);

    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedFlag", ignore = true)
    Account registerRequestToAccount(RegisterRequest registerRequest);

    RegisterResponse accountToRegisterResponse(Account account);

    LoginResponse accountToLoginResponse(Account account);

    @Mapping(target = "nickname", source = "name")
    Account googleUerInfoResponseToAccount(GoogleUerInfo googleUerInfo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "age", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    @Mapping(target = "profileImage", ignore = true)
    @Mapping(target = "birthyear", ignore = true)
    @Mapping(target = "mobile", ignore = true)
    Account naverUserInfoToAccount(NaverUserInfoResponse naverUserInfoResponse);

    default Account kakaoUserInfoToAccount(KakaoUerInfoResponse kakaoUserInfoResponse) {
        return Account.builder().build();
    }
}
