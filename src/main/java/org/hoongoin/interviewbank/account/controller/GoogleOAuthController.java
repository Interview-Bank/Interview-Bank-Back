package org.hoongoin.interviewbank.account.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.GoogleOAuthService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.hoongoin.interviewbank.utils.SecurityUtil.setAuthentication;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth/google")
public class GoogleOAuthController {

	private final GoogleOAuthService googleOAuthService;
	private final AccountMapper accountMapper;
	private final SessionRepository sessionRepository;

	@GetMapping("/login")
	public ResponseEntity<Object> getGoogleLoginUrl(HttpSession session) {
		URI authUri = googleOAuthService.getGoogleLoginUri(session.getId());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> googleLoginOrRegister(HttpSession session,
		@RequestParam(name = "code") String authorizationCode, @RequestParam(name = "state") String state){
		if(sessionRepository.findById(state) == null) {
			log.info("Invalid session id");
			throw new IbUnauthorizedException("Invalid session id");
		}
		Account account = googleOAuthService.googleLoginOrRegister(authorizationCode);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
