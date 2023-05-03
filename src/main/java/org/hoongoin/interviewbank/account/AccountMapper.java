package org.hoongoin.interviewbank.account;

import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.GetMeResponse;
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
			accountEntity.getDeletedAt(), accountEntity.getDeletedFlag(), accountEntity.getAccountType(),
			accountEntity.getPasswordUpdatedAt(), accountEntity.getImageUrl());
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

	GetMeResponse accountToGetMeResponse(Account account);
}
