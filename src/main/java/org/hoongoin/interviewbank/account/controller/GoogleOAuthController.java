package org.hoongoin.interviewbank.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.GoogleOAuthService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hoongoin.interviewbank.utils.SecurityUtil.setAuthentication;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth/google")
public class GoogleOAuthController {

	private final GoogleOAuthService googleOAuthService;
	private final AccountMapper accountMapper;

	@GetMapping("/login")
	public ResponseEntity<Object> getGoogleLoginUrl() throws URISyntaxException {
		URI authUri = googleOAuthService.getGoogleLoginUrI();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> googleLoginOrRegister(
		@RequestParam(name = "authorization-code") String authorizationCode) throws JsonProcessingException {
		Account account = googleOAuthService.googleLoginOrRegister(authorizationCode);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}