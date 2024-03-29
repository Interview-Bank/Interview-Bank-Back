package org.hoongoin.interviewbank.account.application;

import java.time.LocalDateTime;
import java.util.Objects;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.ModifyNicknameRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.response.GetMeResponse;
import org.hoongoin.interviewbank.account.controller.response.InitializeProfileImageResponse;
import org.hoongoin.interviewbank.account.controller.response.ModifyNicknameResponse;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.controller.response.UploadProfileImageResponse;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenCommand;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenQuery;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbUnauthorizedException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {

	private final AccountCommandService accountCommandService;
	private final AccountQueryService accountQueryService;
	private final AccountMapper accountMapper;
	private final BCryptPasswordEncoder passwordEncoder;
	private final PasswordResetTokenProvider passwordResetTokenProvider;
	private final PasswordResetTokenCommand passwordResetTokenCommand;
	private final PasswordResetTokenQuery passwordResetTokenQuery;
	private final AccountExternalService accountExternalService;

	private static final String DEFAULT_IMAGE_URL = "https://interviewbank.s3.us-west-2.amazonaws.com/BasicProfilePhoto.png";

	public RegisterResponse registerByRegisterRequest(RegisterRequest registerRequest) {
		Account account = accountMapper.registerRequestToAccount(registerRequest);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setAccountType(AccountType.EMAIL);
		account.setPasswordUpdatedAt(LocalDateTime.now());
		account.setImageUrl(DEFAULT_IMAGE_URL);

		account = accountCommandService.insertAccount(account);

		return accountMapper.accountToRegisterResponse(account);
	}

	public Account loginByLoginRequest(LoginRequest loginRequest) {
		Account account;
		try {
			account = accountQueryService.findAccountByEmailAndAccountType(loginRequest.getEmail(), AccountType.EMAIL);
		} catch (IbEntityNotFoundException e) {
			log.info("Account of Email {} Not Exists", loginRequest.getEmail());
			throw new IbUnauthorizedException("Email or Password is not correct");
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
			log.info("Password of Account {} is not correct", account.getAccountId());
			throw new IbUnauthorizedException("Email or Password is not correct");
		}

		return account;
	}

	public void createPasswordResetTokenAndSendEmailByRequest(SendEmailRequest sendEmailRequest) {
		Account account = accountQueryService.findAccountByEmailAndAccountType(sendEmailRequest.getEmail(),
			AccountType.EMAIL);

		String hashedToken = passwordResetTokenProvider.createToken();
		passwordResetTokenCommand.saveToken(account.getAccountId(), sendEmailRequest.getEmail(), hashedToken);

		accountExternalService.sendMailTo(sendEmailRequest.getEmail(), hashedToken);
	}

	public boolean validateToken(String token) {
		return passwordResetTokenQuery.existsPasswordResetTokenByHashedToken(token);
	}

	@Transactional
	public void resetPasswordByTokenAndRequest(String token, ResetPasswordRequest resetPasswordRequest) {
		PasswordResetToken passwordResetToken = passwordResetTokenQuery.findResetTokenByToken(token);
		long requestingAccountId = passwordResetToken.getAccountId();

		if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getNewPasswordCheck())) {
			log.info("Password and Password Check is not same");
			throw new IbValidationException("Password and Password Check is not same");
		}

		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPasswordCheck());
		accountCommandService.resetPassword(requestingAccountId, encodedPassword);
	}

	@Transactional
	public void resetPasswordByRequestAndRequestingAccountId(ResetPasswordRequest resetPasswordRequest,
		long requestingAccountId) {
		if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getNewPasswordCheck())) {
			log.info("Password and Password Check is not same");
			throw new IbValidationException("Password and Password Check is not same");
		}

		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPasswordCheck());
		accountCommandService.resetPassword(requestingAccountId, encodedPassword);
	}

	public GetMeResponse getMe(long requestingAccountId) {
		Account account = accountQueryService.findAccountByAccountId(requestingAccountId);
		return accountMapper.accountToGetMeResponse(account);
	}

	@Transactional
	public ModifyNicknameResponse modifyNicknameByRequest(ModifyNicknameRequest modifyNicknameRequest,
		long requestingAccountId) {
		Account account = accountCommandService.modifyNickname(modifyNicknameRequest, requestingAccountId);
		return new ModifyNicknameResponse(account.getNickname(), account.getEmail(), account.getPasswordUpdatedAt());
	}

	@Transactional
	public UploadProfileImageResponse saveProfileImage(MultipartFile multipartFile,
		long requestingAccountId) {
		Account originalAccount = accountQueryService.findAccountByAccountId(requestingAccountId);

		if (multipartFile == null) {
			return new UploadProfileImageResponse(originalAccount.getImageUrl());
		}

		if (!originalAccount.getImageUrl().equals(DEFAULT_IMAGE_URL)) {
			accountExternalService.checkImageUrlOfAccount(originalAccount);
		}

		String uploadedUrl = accountExternalService.uploadImageFile(multipartFile);

		Account updatedAccount = accountCommandService.updateImageUrl(requestingAccountId, uploadedUrl);
		return new UploadProfileImageResponse(updatedAccount.getImageUrl());
	}

	@Transactional
	public InitializeProfileImageResponse saveDefaultProfileImage(long requestingAccountId) {
		return new InitializeProfileImageResponse(
			accountCommandService.updateImageUrl(requestingAccountId, DEFAULT_IMAGE_URL).getImageUrl());
	}
}
