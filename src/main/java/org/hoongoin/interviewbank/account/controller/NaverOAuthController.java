package org.hoongoin.interviewbank.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.NaverOAuthService;
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
@RequestMapping("/account/oauth/naver")
public class NaverOAuthController {

	private final NaverOAuthService naverOAuthService;
	private final AccountMapper accountMapper;

	@GetMapping("/login")
	public ResponseEntity<Object> getNaverLoginUrl() throws URISyntaxException {
		URI authUri = naverOAuthService.getNaverLoginUri();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> googleLoginOrRegister(
		@RequestParam(name = "code") String authorizationCode, @RequestParam(name = "state") String state)
		throws JsonProcessingException {
		Account account = naverOAuthService.naverLoginOrRegister(authorizationCode, state);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
