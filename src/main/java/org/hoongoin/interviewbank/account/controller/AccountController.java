package org.hoongoin.interviewbank.account.controller;

import javax.servlet.http.HttpSession;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.LoginResponse;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.application.AccountService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.config.IbUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok().body(accountService.registerByRegisterRequest(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Account account = accountService.loginByLoginRequest(loginRequest);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new IbUserDetails(account.getEmail(), account.getAccountId(), account.getPassword()),
                account.getPassword(),
                null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok(accountMapper.accountToLoginResponse(account));
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/reset-password/send-email")
    public ResponseEntity<Object> sendEmailToResetPassword(@RequestBody SendEmailRequest sendEmailRequest) {
        accountService.createPasswordResetTokenAndSendEmailByRequest(sendEmailRequest);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/reset-password/token-validation")
    public ResponseEntity<Boolean> resetPasswordTokenValid(@RequestParam(value = "token") String token) {
        return ResponseEntity.ok(accountService.validateToken(token));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam(value = "token") String token,
                                                @RequestBody ResetPasswordRequest resetPasswordRequest) {
        accountService.resetPasswordByTokenAndRequest(token, resetPasswordRequest);
        return ResponseEntity.ok(null);
    }
}
