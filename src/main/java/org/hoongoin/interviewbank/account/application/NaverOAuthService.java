package org.hoongoin.interviewbank.account.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.hoongoin.interviewbank.account.application.dto.NaverTokenRequestParams;
import org.hoongoin.interviewbank.account.application.dto.NaverTokenResponse;
import org.hoongoin.interviewbank.account.application.dto.NaverUserInfoResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
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
    private String scopes;

    public URI getNaverLoginUri() throws URISyntaxException {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", naverClientId);
        params.put("redirect_uri", naverRedirectUri);
        params.put("response_type", "code");
        params.put("state", "RAMDOM_STRING");
        params.put("scope", scopes);

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        String authUrl = naverAuthUri + "?" + paramStr;
        return new URI(authUrl);
    }

    public Account naverLoginOrRegister(String authorizationCode, String state) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        NaverTokenRequestParams requestParams = NaverTokenRequestParams.builder()
                .clientId(naverClientId)
                .clientSecret(naverClientSecret)
                .code(authorizationCode)
                .grantType("authorization_code")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> tokenResponseJson = restTemplate.postForEntity(naverTokenUri, httpRequestEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        NaverTokenResponse naverTokenResponse = objectMapper.readValue(tokenResponseJson.getBody(), new TypeReference<NaverTokenResponse>() {
        });

        HttpHeaders getProfileHeaders = new HttpHeaders();
        getProfileHeaders.set("Authorization", "Bearer " + naverTokenResponse.getAccessToken());
        getProfileHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                naverProfileUri,
                HttpMethod.GET,
                entity,
                String.class
        );
        NaverUserInfoResponse naverUserInfoResponse = objectMapper.readValue(tokenResponseJson.getBody(), new TypeReference<NaverUserInfoResponse>() {
        });

        return accountCommandService.saveNaverUserInfoIfNotExists(naverUserInfoResponse);
    }
}
