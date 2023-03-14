package org.hoongoin.interviewbank.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.KakaoOAuthService;
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
@RequestMapping("/account/oauth/kakao")
public class KakaoOAuthController {

	private final KakaoOAuthService kakaoOAuthService;
	private final AccountMapper accountMapper;

	@GetMapping("/login")
	public ResponseEntity<Object> getKakaoLoginUrl() throws URISyntaxException {
		URI authUri = kakaoOAuthService.getKakaoLoginUri();
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> kakaoLoginOrRegister(
		@RequestParam(name = "code") String authorizationCode, @RequestParam(name = "state") String state)
		throws JsonProcessingException, URISyntaxException {
		Account account = kakaoOAuthService.kakaoLoginOrRegister(authorizationCode);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
