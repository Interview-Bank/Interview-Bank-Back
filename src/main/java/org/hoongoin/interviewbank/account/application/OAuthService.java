package org.hoongoin.interviewbank.account.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.exception.IbServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OAuthService {

    private final AccountCommandService accountCommandService;
    @Value("{spring.oauth2.client.registration.google.auth-uri}")
    private String googleAuthUri;
    @Value("{spring.oauth2.client.registration.google.client-id}")
    private String googleClientId;
    @Value("{spring.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;
    @Value("{spring.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;
    @Value("${spring.oauth2.client.registration.google.scope}")
    private String scopes;

    public URI getGoogleLoginUrI() throws URISyntaxException {
        Map<String, Object> params = new HashMap<>();
        params.put("client_id", googleClientId);
        params.put("redirect_uri", googleRedirectUri);
        params.put("response_type", "code");
        params.put("scope", scopes);

        String paramStr = params.entrySet().stream()
                .map(param -> param.getKey() + "=" + param.getValue())
                .collect(Collectors.joining("&"));

        String authUrl = googleAuthUri + "/o/oauth2/v2/auth" + "?" + paramStr;
        return new URI(authUrl);
    }

    public Account googleLoginOrRegister(String authorizationCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        GoogleTokenRequest requestParams = GoogleTokenRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authorizationCode)
                .redirectUri(googleRedirectUri)
                .grantType("authorization_code")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GoogleTokenRequest> httpRequestEntity = new HttpEntity<>(requestParams, headers);
        ResponseEntity<String> apiResponseJson = restTemplate.postForEntity(googleAuthUri + "/token", httpRequestEntity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        GoogleTokenResponse googleLoginResponse = objectMapper.readValue(apiResponseJson.getBody(), new TypeReference<GoogleTokenResponse>() {});

        String jwtToken = googleLoginResponse.getIdToken();

        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUri + "/tokeninfo").queryParam("id_token", jwtToken).toUriString();

        String resultJson = restTemplate.getForObject(requestUrl, String.class);
        if(resultJson == null){
            throw new IbServerErrorException("Google OAuth Failed");
        }
        GoogleUerInfoResponse googleUerInfoResponse = objectMapper.readValue(resultJson, new TypeReference<GoogleUerInfoResponse>() {});
        return accountCommandService.saveGoogleUserIfNotExists(googleUerInfoResponse);
    }
}
