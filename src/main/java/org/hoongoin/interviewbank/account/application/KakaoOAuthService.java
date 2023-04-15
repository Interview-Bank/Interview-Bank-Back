package org.hoongoin.interviewbank.account.application;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.application.dto.*;
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

@RequiredArgsConstructor
@Service
public class KakaoOAuthService {

	private final AccountCommandService accountCommandService;
	private final KakaoOAuthProperties kakaoOAuthProperties;
	private final RestTemplate restTemplate;

	public URI getKakaoLoginUri() {
		return UriComponentsBuilder.fromHttpUrl(kakaoOAuthProperties.getAuthUri())
			.queryParam("client_id", kakaoOAuthProperties.getClientId())
			.queryParam("redirect_uri", kakaoOAuthProperties.getRedirectUri())
			.queryParam("response_type", "code")
			.build()
			.toUri();
	}

	public Account kakaoLoginOrRegister(String authorizationCode) {
		KakaoTokenResponse kakaoTokenResponse = exchangeCodeForAccessToken(authorizationCode);
		KakaoUserInfoResponse kakaoUserInfoResponse = getKakaoProfileResponse(kakaoTokenResponse.getAccessToken());
		Account account = getAccount(kakaoUserInfoResponse);
		return accountCommandService.insertIfNotExists(account);
	}

	private KakaoTokenResponse exchangeCodeForAccessToken(String authorizationCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoOAuthProperties.getClientId());
		body.add("client_secret", kakaoOAuthProperties.getClientSecret());
		body.add("code", authorizationCode);
		body.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());

		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);

		KakaoTokenResponse kakaoTokenResponse = restTemplate.postForObject(
			kakaoOAuthProperties.getTokenUri(),
			kakaoTokenRequest,
			KakaoTokenResponse.class);
		if (kakaoTokenResponse == null) {
			throw new IbInternalServerException("Failed to get access token from Kakao");
		}
		return kakaoTokenResponse;
	}

	private KakaoUserInfoResponse getKakaoProfileResponse(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

		return restTemplate.exchange(
				kakaoOAuthProperties.getProfileUri(),
				HttpMethod.POST,
				kakaoProfileRequest,
				KakaoUserInfoResponse.class)
			.getBody();
	}

	private Account getAccount(KakaoUserInfoResponse kakaoUserInfoResponse) {
		return Account.builder()
			.accountType(AccountType.KAKAO)
			.email(kakaoUserInfoResponse.getKakao_account().getEmail())
			.nickname(kakaoUserInfoResponse.getProperties().getNickname())
			.imageUrl(kakaoUserInfoResponse.getProperties().getProfile_image())
			.build();
	}
}
