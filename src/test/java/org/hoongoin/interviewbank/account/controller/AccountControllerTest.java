package org.hoongoin.interviewbank.account.controller;

import static org.assertj.core.api.Assertions.*;

import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.LoginResponse;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.config.IbSpringBootTest;
import org.hoongoin.interviewbank.config.IbUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@IbSpringBootTest
class AccountControllerTest {

	@Autowired
	private AccountController accountController;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Test
	void register_Success() {
		//given
		RegisterRequest registerRequest = new RegisterRequest("test", "test@example.com", "test");

		//when
		ResponseEntity<RegisterResponse> registerResponse = accountController.register(registerRequest);

		//then
		assertThat(registerResponse.getBody().getEmail()).isEqualTo(registerRequest.getEmail());
		assertThat(registerResponse.getBody().getNickname()).isEqualTo(registerRequest.getNickname());
	}

	@Test
	void login_Success() {
		//given
		AccountEntity accountEntity = AccountEntity.builder()
			.email("test@example.com")
			.nickname("test")
			.accountType(AccountType.EMAIL)
			.password(passwordEncoder.encode("test"))
			.build();
		accountEntity = accountRepository.saveAndFlush(accountEntity);

		LoginRequest loginRequest = new LoginRequest("test@example.com", "test");

		//when
		ResponseEntity<LoginResponse> loginResponse = accountController.login(loginRequest);

		//then
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		IbUserDetails ibUserDetails = (IbUserDetails)authentication.getPrincipal();

		assertThat(authentication.isAuthenticated()).isTrue();
		assertThat(ibUserDetails.getUsername()).isEqualTo(accountEntity.getEmail());
		assertThat(ibUserDetails.getAccountId()).isEqualTo(accountEntity.getId());
		assertThat(passwordEncoder.matches("test", ibUserDetails.getPassword())).isTrue();

		assertThat(loginResponse.getBody().getAccountId()).isEqualTo(accountEntity.getId());
		assertThat(loginResponse.getBody().getNickname()).isEqualTo(accountEntity.getNickname());
	}
}
