package org.hoongoin.interviewbank.account;

import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	@Mapping(target = "accountId", source = "accountEntity.id")
	Account accountEntityToAccount(AccountEntity accountEntity);

	@Mapping(target = "id", source = "account.accountId")
	AccountEntity accountToAccountEntity(Account account);

	Account registerRequestToAccount(RegisterRequest registerRequest);

	AccountEntity registerRequestToAccountEntity(RegisterRequest registerRequest);

	RegisterResponse accountToRegisterResponse(Account account);
}
