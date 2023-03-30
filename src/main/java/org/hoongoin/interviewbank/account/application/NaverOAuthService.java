package org.hoongoin.interviewbank.account.application;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.application.dto.NaverTokenRequestParams;
import org.hoongoin.interviewbank.account.application.dto.NaverTokenResponse;
import org.hoongoin.interviewbank.account.application.dto.NaverProfileResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NaverOAuthService {

	private final AccountCommandService accountCommandService;
	@Value("${spring.oauth2.client.registration.naver.auth-uri}")
	private String naverAuthUri;
	@Value("${spring.oauth2.client.registration.naver.token-uri}")
	private String naverTokenUri;
	@Value("${spring.oauth2.client.registration.naver.profile-uri}")
	private String naverProfileUri;
	@Value("${spring.oauth2.client.registration.naver.client-id}")
	private String naverClientId;
	@Value("${spring.oauth2.client.registration.naver.client-secret}")
	private String naverClientSecret;
	@Value("${spring.oauth2.client.registration.naver.redirect-uri}")
	private String naverRedirectUri;
	@Value("${spring.oauth2.client.registration.naver.scope}")
	private List<String> scopes;

	public URI getNaverLoginUri(String sessionId) {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("client_id", naverClientId);
		queryParams.put("redirect_uri", naverRedirectUri);
		queryParams.put("response_type", "code");
		queryParams.put("state", sessionId);
		queryParams.put("scope", String.join("%20", scopes));

		String queryString = queryParams.entrySet().stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));

		String authUrl = naverAuthUri + "?" + queryString;
		try {
			return new URI(authUrl);
		} catch (URISyntaxException e) {
			throw new IbInternalServerException("URISyntaxException");
		}
	}

	public Account naverLoginOrRegister(String authorizationCode, String state, String sessionId) {
		if (!state.equals(sessionId)) {
			throw new IbUnauthorizedException("Session Changed");
		}

		NaverTokenResponse naverTokenResponse = exchangeCodeForAccessToken(authorizationCode, state);
		NaverProfileResponse naverProfileResponse = getNaverProfileResponse(naverTokenResponse.getAccessToken());
		Account account = getAccount(naverProfileResponse);
		return accountCommandService.insertIfNotExists(account);
	}

	private NaverTokenResponse exchangeCodeForAccessToken(String authorizationCode, String state) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		NaverTokenRequestParams requestParams = NaverTokenRequestParams.builder()
			.clientId(naverClientId)
			.clientSecret(naverClientSecret)
			.code(authorizationCode)
			.grantType("authorization_code")
			.state(state)
			.build();

		HttpEntity<NaverTokenRequestParams> httpEntity = new HttpEntity<>(requestParams, headers);

		ResponseEntity<NaverTokenResponse> tokenResponseEntity = restTemplate.postForEntity(
			naverTokenUri,
			httpEntity,
			NaverTokenResponse.class);

		if(!tokenResponseEntity.hasBody()){
			throw new IbInternalServerException("Google OAuth Failed");
		}
		return tokenResponseEntity.getBody();
	}

	private NaverProfileResponse getNaverProfileResponse(String accessToken){
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> httpRequestEntity = new HttpEntity<>(headers);
		ResponseEntity<NaverProfileResponse> httpResponseEntity = restTemplate.exchange(
			naverProfileUri,
			HttpMethod.GET,
			httpRequestEntity,
			NaverProfileResponse.class
		);

		if(!httpResponseEntity.hasBody()){
			throw new IbInternalServerException("Naver OAuth Failed");
		}
		return httpResponseEntity.getBody();
	}


	private Account getAccount(NaverProfileResponse naverProfileResponse) {
		return Account.builder()
			.nickname(naverProfileResponse.getResponse().getNickname())
			.email(naverProfileResponse.getResponse().getEmail())
			.accountType(AccountType.NAVER)
			.build();
	}
}
