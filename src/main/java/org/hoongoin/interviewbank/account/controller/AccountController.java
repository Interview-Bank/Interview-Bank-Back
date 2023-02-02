package org.hoongoin.interviewbank.account.controller;

import javax.servlet.http.HttpSession;

import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.service.AccountService;
import org.hoongoin.interviewbank.account.service.domain.Account;
import org.hoongoin.interviewbank.config.IbUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok().body(accountService.registerByRegisterRequest(registerRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
		Account account = accountService.loginByLoginRequest(loginRequest);
		Authentication authentication = new UsernamePasswordAuthenticationToken(new IbUserDetails(account.getEmail(),
			account.getAccountId(), account.getPassword()),
			account.getPassword(), null);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return ResponseEntity.ok(null);
	}

	@PostMapping("/logout")
	public ResponseEntity<Object> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok(null);
	}
}