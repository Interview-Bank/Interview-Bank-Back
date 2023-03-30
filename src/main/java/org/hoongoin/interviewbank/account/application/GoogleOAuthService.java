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

	public URI getGoogleLoginUrI(String sessionId) {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("client_id", googleClientId);
		queryParams.put("response_type", "code");
		queryParams.put("scope", String.join("%20", scopes));
		queryParams.put("redirect_uri", googleRedirectUri);
		queryParams.put("state", sessionId);
		queryParams.put("nonce", "0394852-3190485-2490358");  // TODO : used once random nonce 만들기
		
		String queryString = queryParams.entrySet().stream()
			.map(entry -> entry.getKey() + "=" + entry.getValue())
			.collect(Collectors.joining("&"));

		String authUrl = googleAuthUri + "?" + queryString;
		try {
			return new URI(authUrl);
		} catch (URISyntaxException e) {
			throw new IbInternalServerException("URISyntaxException");
		}
	}

	public Account googleLoginOrRegister(String authorizationCode) {
		GoogleTokenResponse googleTokenResponse = exchangeCodeForAccessTokenAndIdToken(authorizationCode);
		Account account = getUserInfoIn(googleTokenResponse.getIdToken());
		return accountCommandService.insertIfNotExists(account);
	}

	private GoogleTokenResponse exchangeCodeForAccessTokenAndIdToken(String authorizationCode) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		GoogleTokenRequestParams requestParams = GoogleTokenRequestParams.builder()
			.code(authorizationCode)
			.clientId(googleClientId)
			.clientSecret(googleClientSecret)
			.redirectUri(googleRedirectUri)
			.grantType("authorization_code")
			.build();

		HttpEntity<GoogleTokenRequestParams> httpEntity = new HttpEntity<>(requestParams, headers);

		ResponseEntity<GoogleTokenResponse> tokenResponseEntity = restTemplate.postForEntity(
			googleTokenUri,
			httpEntity,
			GoogleTokenResponse.class);

		if(!tokenResponseEntity.hasBody()){
			throw new IbInternalServerException("Google OAuth Failed");
		}
		return tokenResponseEntity.getBody();
	}

	private Account getUserInfoIn(String jwt){
		String[] jwtParts = jwt.split("\\.");
		String claimsJson = new String(Base64.getDecoder().decode(jwtParts[1]));

		Gson gson = new Gson();
		GoogleJwtPayload googleJwtPayload = gson.fromJson(claimsJson, GoogleJwtPayload.class);

		return Account.builder()
			.email(googleJwtPayload.getEmail())
			.nickname(googleJwtPayload.getName())
			.accountType(AccountType.GOOGLE)
			.build();
	}
}
