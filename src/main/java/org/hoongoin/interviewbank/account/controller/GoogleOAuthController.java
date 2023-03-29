package org.hoongoin.interviewbank.account.controller;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.GoogleOAuthService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
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
	private final SessionRepository sessionRepository;

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

		Session storedSession = sessionRepository.findById(state);
		if (storedSession == null) {
			throw new IbUnauthorizedException("Session Changed");
		}
		Account account = googleOAuthService.googleLoginOrRegister(authorizationCode);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
