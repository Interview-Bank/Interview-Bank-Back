package org.hoongoin.interviewbank.account.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hoongoin.interviewbank.account.application.dto.NaverOAuthProperties;
import org.hoongoin.interviewbank.account.application.dto.NaverProfileResponse;
import org.hoongoin.interviewbank.account.application.dto.NaverTokenResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NaverOAuthService {

	private final AccountCommandService accountCommandService;
	private final RestTemplate restTemplate;
	private final NaverOAuthProperties naverOAuthProperties;

	public URI getNaverLoginUri(String sessionId) {
		return UriComponentsBuilder.fromHttpUrl(naverOAuthProperties.getAuthUri())
			.queryParam("client_id", naverOAuthProperties.getClientId())
			.queryParam("redirect_uri", naverOAuthProperties.getRedirectUri())
			.queryParam("response_type", "code")
			.queryParam("state", sessionId)
			.queryParam("scope", String.join(" ", naverOAuthProperties.getScopes()))
			.build()
			.encode()
			.toUri();
	}

	public Account naverLoginOrRegister(String authorizationCode, String state) throws JsonProcessingException {
		NaverTokenResponse naverTokenResponse = exchangeCodeForAccessToken(authorizationCode, state);
		NaverProfileResponse naverProfileResponse = getNaverProfileResponse(naverTokenResponse.getAccessToken());
		Account account = getAccount(naverProfileResponse);
		return accountCommandService.insertIfNotExists(account);
	}

	private NaverTokenResponse exchangeCodeForAccessToken(String authorizationCode, String state) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", naverOAuthProperties.getClientId());
		body.add("client_secret", naverOAuthProperties.getClientSecret());
		body.add("code", authorizationCode);
		body.add("state", state);

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);

		NaverTokenResponse naverTokenResponse = restTemplate.exchange(
			naverOAuthProperties.getTokenUri(),
			HttpMethod.POST,
			naverTokenRequest,
			NaverTokenResponse.class
		).getBody();

		if (naverTokenResponse == null) {
			throw new IbInternalServerException("Naver OAuth Failed");
		}
		return naverTokenResponse;
	}

	private NaverProfileResponse getNaverProfileResponse(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> httpRequestEntity = new HttpEntity<>(headers);

		ResponseEntity<NaverProfileResponse> httpResponseEntity = restTemplate.exchange(
			naverOAuthProperties.getProfileUri(),
			HttpMethod.GET,
			httpRequestEntity,
			NaverProfileResponse.class
		);

		if (!httpResponseEntity.getStatusCode().equals(HttpStatus.OK) || !httpResponseEntity.hasBody()) {
			throw new IbInternalServerException("Naver OAuth Failed");
		}

		return httpResponseEntity.getBody();
	}

	private Account getAccount(NaverProfileResponse naverProfileResponse) {
		return Account.builder()
			.nickname(naverProfileResponse.getResponse().getNickname())
			.email(naverProfileResponse.getResponse().getEmail())
			.accountType(AccountType.NAVER)
			.imageUrl(naverProfileResponse.getResponse().getProfileImage())
			.build();
	}
}
