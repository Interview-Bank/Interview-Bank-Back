package org.hoongoin.interviewbank.account.service;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.exception.IbPasswordNotMatchException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountService {

	private final AccountCommandService accountCommandService;
	private final AccountQueryService accountQueryService;
	private final AccountMapper accountMapper;
	private final BCryptPasswordEncoder passwordEncoder;

	public RegisterResponse registerByRegisterRequest(RegisterRequest registerRequest){
		Account account = accountMapper.registerRequestToAccount(registerRequest);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account = accountCommandService.insertAccount(account);
		return accountMapper.accountToRegisterResponse(account);
	}

	public Account loginByLoginRequest(LoginRequest loginRequest) {
		Account account = accountQueryService.findEntityByEmail(loginRequest.getEmail());
		if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
			throw new IbPasswordNotMatchException();
		}
		return account;
	}
}
