package org.hoongoin.interviewbank.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.hoongoin.interviewbank.account.application.OAuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/google/login/url")
    public ResponseEntity<Object> getGoogleLoginUrl() throws URISyntaxException {
        URI authUri = oAuthService.getGoogleLoginUrI();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(authUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @PostMapping("/google/login")
    public ResponseEntity<Object> googleLoginOrRegister(@RequestParam(name = "authorization-code") String authorizationCode) throws JsonProcessingException {
        return ResponseEntity.ok(oAuthService.googleLoginOrRegister(authorizationCode));
    }
}
