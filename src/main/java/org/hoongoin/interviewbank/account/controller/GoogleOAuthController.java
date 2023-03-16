package org.hoongoin.interviewbank.account.controller;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.GoogleOAuthService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.hoongoin.interviewbank.utils.SecurityUtil.setAuthentication;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth/google")
public class GoogleOAuthController {

	private final GoogleOAuthService googleOAuthService;
	private final AccountMapper accountMapper;

	@GetMapping("/login")
	public ResponseEntity<Object> getGoogleLoginUrl(HttpSession session) {
		URI authUri = googleOAuthService.getGoogleLoginUrI(session.getId());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> googleLoginOrRegister(HttpSession session,
		@RequestParam(name = "code") String authorizationCode, @RequestParam(name = "state") String state){
		Account account = googleOAuthService.googleLoginOrRegister(authorizationCode, state, session.getId());
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
