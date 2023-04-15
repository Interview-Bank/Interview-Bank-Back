package org.hoongoin.interviewbank.account.application;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.application.dto.*;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;

@RequiredArgsConstructor
@Service
public class GoogleOAuthService {

	private final AccountCommandService accountCommandService;
	private final GoogleOAuthProperties googleOAuthProperties;
	private final RestTemplate restTemplate;

	public URI getGoogleLoginUri(String sessionId) {
		return UriComponentsBuilder.fromHttpUrl(googleOAuthProperties.getAuthUri())
			.queryParam("client_id", googleOAuthProperties.getClientId())
			.queryParam("response_type", "code")
			.queryParam("scope", String.join("%20", googleOAuthProperties.getScopes()))
			.queryParam("redirect_uri", googleOAuthProperties.getRedirectUri())
			.queryParam("state", sessionId)
			.queryParam("nonce", "0394852-3190485-2490358")
			.build()
			.encode()
			.toUri();
	}

	public Account googleLoginOrRegister(String authorizationCode) {
		GoogleTokenResponse googleTokenResponse = exchangeCodeForAccessTokenAndIdToken(authorizationCode);
		Account account = getUserInfoIn(googleTokenResponse.getIdToken());
		return accountCommandService.insertIfNotExists(account);
	}

	private GoogleTokenResponse exchangeCodeForAccessTokenAndIdToken(String authorizationCode) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		GoogleTokenRequestParams requestParams = GoogleTokenRequestParams.builder()
			.code(authorizationCode)
			.clientId(googleOAuthProperties.getClientId())
			.clientSecret(googleOAuthProperties.getClientSecret())
			.redirectUri(googleOAuthProperties.getRedirectUri())
			.grantType("authorization_code")
			.build();

		HttpEntity<GoogleTokenRequestParams> httpEntity = new HttpEntity<>(requestParams, headers);

		ResponseEntity<GoogleTokenResponse> tokenResponseEntity = restTemplate.postForEntity(
			googleOAuthProperties.getTokenUri(),
			httpEntity,
			GoogleTokenResponse.class);

		if (!tokenResponseEntity.hasBody()) {
			throw new IbInternalServerException("Google OAuth Failed");
		}
		return tokenResponseEntity.getBody();
	}

	private Account getUserInfoIn(String jwt) {
		String[] jwtParts = jwt.split("\\.");
		String claimsJson = new String(Base64.getDecoder().decode(jwtParts[1]));

		Gson gson = new Gson();
		GoogleJwtPayload googleJwtPayload = gson.fromJson(claimsJson, GoogleJwtPayload.class);

		return Account.builder()
			.email(googleJwtPayload.getEmail())
			.nickname(googleJwtPayload.getName())
			.accountType(AccountType.GOOGLE)
			.imageUrl(googleJwtPayload.getPicture())
			.build();
	}
}
