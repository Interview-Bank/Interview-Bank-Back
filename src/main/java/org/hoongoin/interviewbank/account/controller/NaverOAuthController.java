package org.hoongoin.interviewbank.account.controller;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.NaverOAuthService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.hoongoin.interviewbank.utils.SecurityUtil.setAuthentication;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth/naver")
public class NaverOAuthController {

	private final NaverOAuthService naverOAuthService;
	private final AccountMapper accountMapper;
	private final SessionRepository sessionRepository;

	@GetMapping("/login")
	public ResponseEntity<Object> getNaverLoginUrl(HttpSession session) {
		URI authUri = naverOAuthService.getNaverLoginUri(session.getId());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(authUri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}

	@PostMapping("/login/redirect")
	public ResponseEntity<Object> naverLoginOrRegister(HttpSession session,
		@RequestParam(name = "code") String authorizationCode, @RequestParam(name = "state") String state,
		@RequestParam(name = "error") String error, @RequestParam(name = "error_description") String errorDescription) {
		if(!error.isEmpty()){
			throw new IbInternalServerException(error + errorDescription);
		}

		if(sessionRepository.findById(state) == null) {
			throw new IbUnauthorizedException("Invalid session id");
		}
		Account account = naverOAuthService.naverLoginOrRegister(authorizationCode);
		setAuthentication(account);
		return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
	}
}
