package org.hoongoin.interviewbank.account.controller;

import javax.servlet.http.HttpSession;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.ModifyNicknameRequest;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.response.GetMeResponse;
import org.hoongoin.interviewbank.account.controller.response.InitializeProfileImageResponse;
import org.hoongoin.interviewbank.account.controller.response.LoginResponse;
import org.hoongoin.interviewbank.account.controller.response.ModifyNicknameResponse;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.application.AccountService;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.controller.response.UploadProfileImageResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import static org.hoongoin.interviewbank.utils.SecurityUtil.getRequestingAccountId;
import static org.hoongoin.interviewbank.utils.SecurityUtil.setAuthentication;

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
		setAuthentication(account);
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

	@PostMapping("/reset-password-with-token")
	public ResponseEntity<Object> resetPasswordWithToken(@RequestParam(value = "token") String token,
		@RequestBody ResetPasswordRequest resetPasswordRequest) {
		accountService.resetPasswordByTokenAndRequest(token, resetPasswordRequest);
		return ResponseEntity.ok(null);
	}

	@PostMapping("/reset-password")
	public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
		long requestingAccountId = getRequestingAccountId();
		accountService.resetPasswordByRequestAndRequestingAccountId(resetPasswordRequest, requestingAccountId);
		return ResponseEntity.ok(null);
	}

	@GetMapping("/me")
	public ResponseEntity<GetMeResponse> getMe() {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok(accountService.getMe(requestingAccountId));
	}

	@PutMapping("/nickname")
	public ResponseEntity<ModifyNicknameResponse> modifyNickname(
		@RequestBody ModifyNicknameRequest modifyNicknameRequest) {
		long requestingAccountId = getRequestingAccountId();
		return ResponseEntity.ok()
			.body(accountService.modifyNicknameByRequest(modifyNicknameRequest, requestingAccountId));
	}

	@PostMapping(value = "/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<UploadProfileImageResponse> uploadProfileImage(
		@RequestParam(name = "file", required = false) MultipartFile file) {
		return ResponseEntity.ok()
			.body(accountService.saveProfileImage(file, getRequestingAccountId()));
	}

	@PutMapping(value = "/initialize/profile-image")
	public ResponseEntity<InitializeProfileImageResponse> initializeProfileImage() {
		return ResponseEntity.ok().body(accountService.saveDefaultProfileImage(getRequestingAccountId()));
	}
}
