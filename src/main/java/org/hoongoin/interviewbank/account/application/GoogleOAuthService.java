package org.hoongoin.interviewbank.account.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import lombok.RequiredArgsConstructor;

import org.hoongoin.interviewbank.account.application.dto.*;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
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
public class GoogleOAuthService {

	private final AccountCommandService accountCommandService;
	@Value("${spring.oauth2.client.registration.google.auth-uri}")
	private String googleAuthUri;
	@Value("${spring.oauth2.client.registration.google.token-uri}")
	private String googleTokenUri;
	@Value("${spring.oauth2.client.registration.google.client-id}")
	private String googleClientId;
	@Value("${spring.oauth2.client.registration.google.client-secret}")
	private String googleClientSecret;
	@Value("${spring.oauth2.client.registration.google.redirect-uri}")
	private String googleRedirectUri;
	@Value("${spring.oauth2.client.registration.google.profile-uri}")
	private String googleProfileUri;
	@Value("${spring.oauth2.client.registration.google.scope}")
	private List<String> scopes;

	public URI getGoogleLoginUrI() throws URISyntaxException {
		Map<String, Object> params = new HashMap<>();
		params.put("client_id", googleClientId);
		params.put("redirect_uri", googleRedirectUri);
		params.put("response_type", "code");
		params.put("state", "RAMDOM_STRING");
		params.put("scope", scopes);

		String paramStr = params.entrySet().stream()
			.map(param -> param.getKey() + "=" + param.getValue())
			.collect(Collectors.joining("&"));

		String authUrl = googleAuthUri + "?" + paramStr;
		return new URI(authUrl);
	}

	public Account googleLoginOrRegister(String authorizationCode) throws JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		GoogleTokenRequestParams requestParams = GoogleTokenRequestParams.builder()
			.clientId(googleClientId)
			.clientSecret(googleClientSecret)
			.code(authorizationCode)
			.redirectUri(googleRedirectUri)
			.grantType("authorization_code")
			.build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<GoogleTokenRequestParams> httpRequestEntity = new HttpEntity<>(requestParams, headers);
		ResponseEntity<String> tokenResponseJson = restTemplate.postForEntity(googleTokenUri, httpRequestEntity,
			String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		GoogleTokenResponse googleTokenResponse = objectMapper.readValue(tokenResponseJson.getBody(),
			new TypeReference<GoogleTokenResponse>() {
			});

		HttpHeaders getProfileHeaders = new HttpHeaders();
		getProfileHeaders.set("Authorization", "Bearer " + googleTokenResponse.getAccessToken());
		getProfileHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
			googleProfileUri,
			HttpMethod.GET,
			entity,
			String.class
		);
		GoogleUerInfo googleUerInfo = objectMapper.readValue(tokenResponseJson.getBody(),
			new TypeReference<GoogleUerInfo>() {
			});

		return accountCommandService.saveGoogleUserIfNotExists(googleUerInfo);
	}
}