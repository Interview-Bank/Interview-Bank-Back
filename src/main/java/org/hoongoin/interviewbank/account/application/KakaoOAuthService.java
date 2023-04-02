package org.hoongoin.interviewbank.account.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.application.dto.*;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class KakaoOAuthService {

	private final AccountCommandService accountCommandService;
	@Value("${spring.oauth2.client.registration.kakao.auth-uri}")
	private String kakaoAuthUri;
	@Value("${spring.oauth2.client.registration.kakao.client-id}")
	private String kakaoClientId;
	@Value("${spring.oauth2.client.registration.kakao.client-secret}")
	private String kakaoClientSecret;
	@Value("${spring.oauth2.client.registration.kakao.redirect-uri}")
	private String kakaoRedirectUri;
	@Value("${spring.oauth2.client.registration.kakao.token-uri}")
	private String kakaoTokenUri;
	@Value("${spring.oauth2.client.registration.kakao.profile-uri}")
	private String kakaoProfileUri;

	public URI getKakaoLoginUri() throws URISyntaxException {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", kakaoClientId);
		params.put("redirect_uri", kakaoRedirectUri);
		params.put("response_type", "code");

		String paramStr = params.entrySet().stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));

		String authUrl = kakaoAuthUri + "?" + paramStr;
		return new URI(authUrl);
	}

	public Account kakaoLoginOrRegister(String authorizationCode) throws URISyntaxException, JsonProcessingException {
		String accessToken = exchangeCodeForAccessToken(authorizationCode);
		KakaoUerInfoResponse kakaoUserInfoResponse = getKakaoProfileRespnose(accessToken);
		return accountCommandService.saveKakaoUserIfNotExists(kakaoUserInfoResponse);
	}

	private String exchangeCodeForAccessToken(String authorizationCode) throws JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", kakaoClientId);
		body.add("client_secret", kakaoClientSecret);
		body.add("code", authorizationCode);
		body.add("redirect_uri", kakaoRedirectUri);

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
			new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			kakaoTokenUri,
			HttpMethod.POST,
			naverTokenRequest,
			String.class
		);
		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	private KakaoUerInfoResponse getKakaoProfileRespnose(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

		return restTemplate.exchange(kakaoProfileUri, HttpMethod.POST, kakaoProfileRequest, KakaoUerInfoResponse.class)
			.getBody();
	}
}
