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
        RestTemplate restTemplate = new RestTemplate();
        KakaoTokenRequestParams requestParams = KakaoTokenRequestParams.builder()
                .clientId(kakaoClientId)
                .clientSecret(kakaoClientSecret)
                .code(authorizationCode)
                .redirectUri(kakaoRedirectUri)
                .grantType("authorization_code")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<KakaoTokenRequestParams> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> tokenResponseJson = restTemplate.postForEntity(kakaoTokenUri, httpRequestEntity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(tokenResponseJson.getBody(), new TypeReference<KakaoTokenResponse>() {
        });

        HttpHeaders getProfileHeaders = new HttpHeaders();
        getProfileHeaders.set("Authorization", "Bearer " + kakaoTokenResponse.getAccessToken());
        getProfileHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("charset", "utf-8");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                kakaoProfileUri,
                HttpMethod.GET,
                entity,
                String.class
        );
        KakaoUerInfoResponse kakaoUserInfoResponse = objectMapper.readValue(tokenResponseJson.getBody(), new TypeReference<KakaoUerInfoResponse>() {});

        return accountCommandService.saveKakaoUserIfNotExists(kakaoUserInfoResponse);
    }
}
