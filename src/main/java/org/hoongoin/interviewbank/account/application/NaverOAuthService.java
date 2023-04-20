package org.hoongoin.interviewbank.account.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hoongoin.interviewbank.account.application.dto.NaverProfileResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.common.discord.DiscordHandler;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
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

	public Account naverLoginOrRegister(String authorizationCode, String state) throws JsonProcessingException {
		String accessToken = exchangeCodeForAccessToken(authorizationCode, state);
		NaverProfileResponse naverProfileResponse = getNaverProfileResponse(accessToken);
		Account account = getAccount(naverProfileResponse);
		return accountCommandService.insertIfNotExists(account);
	}

	private String exchangeCodeForAccessToken(String authorizationCode, String state) throws
		JsonProcessingException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", naverClientId);
		body.add("client_secret", naverClientSecret);
		body.add("code", authorizationCode);
		body.add("state", state);

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
			new HttpEntity<>(body, headers);
		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> response = rt.exchange(
			naverTokenUri,
			HttpMethod.POST,
			naverTokenRequest,
			String.class
		);

		String responseBody = response.getBody();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		return jsonNode.get("access_token").asText();
	}

	private NaverProfileResponse getNaverProfileResponse(String accessToken) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> httpRequestEntity = new HttpEntity<>(headers);
		ResponseEntity<NaverProfileResponse> httpResponseEntity = restTemplate.exchange(
			naverProfileUri,
			HttpMethod.GET,
			httpRequestEntity,
			NaverProfileResponse.class
		);

		if (!httpResponseEntity.getStatusCode().equals(HttpStatus.OK) || httpResponseEntity.hasBody()) {
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
