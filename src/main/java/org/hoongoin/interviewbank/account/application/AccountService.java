package org.hoongoin.interviewbank.account.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.request.UploadProfileImageRequest;
import org.hoongoin.interviewbank.account.controller.response.RegisterResponse;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.account.controller.response.UploadProfileImageResponse;
import org.hoongoin.interviewbank.account.domain.AccountCommandService;
import org.hoongoin.interviewbank.account.domain.AccountQueryService;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenCommand;
import org.hoongoin.interviewbank.account.domain.PasswordResetTokenQuery;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.hoongoin.interviewbank.exception.IbPasswordNotMatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

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
	private final MailService mailService;
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;
	@Value("${aws.region.static}")
	private String region;

	public RegisterResponse registerByRegisterRequest(RegisterRequest registerRequest) {
		Account account = accountMapper.registerRequestToAccount(registerRequest);
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		account.setAccountType(AccountType.EMAIL);
		account = accountCommandService.insertAccount(account);
		return accountMapper.accountToRegisterResponse(account);
	}

	public Account loginByLoginRequest(LoginRequest loginRequest) {
		Account account = accountQueryService.findAccountByEmail(loginRequest.getEmail());
		if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
			throw new IbPasswordNotMatchException(account.getEmail());
		}
		return account;
	}

	public void createPasswordResetTokenAndSendEmailByRequest(SendEmailRequest sendEmailRequest) {
		Account account = accountQueryService.findAccountByEmail(sendEmailRequest.getEmail());

		String hashedToken = passwordResetTokenProvider.createToken();
		passwordResetTokenCommand.saveToken(account.getAccountId(), sendEmailRequest.getEmail(), hashedToken);

		mailService.sendMailTo(sendEmailRequest.getEmail(), hashedToken);
	}

	public boolean validateToken(String token) {
		return passwordResetTokenQuery.existsPasswordResetTokenByHashedToken(token);
	}

	@Transactional
	public void resetPasswordByTokenAndRequest(String token, ResetPasswordRequest resetPasswordRequest) {
		PasswordResetToken passwordResetToken = passwordResetTokenQuery.findResetTokenByToken(token);
		long requestingAccountId = passwordResetToken.getAccountId();

		if (!Objects.equals(resetPasswordRequest.getNewPassword(), resetPasswordRequest.getNewPasswordCheck())) {
			throw new IbPasswordNotMatchException("");
		}

		String encodedPassword = passwordEncoder.encode(resetPasswordRequest.getNewPasswordCheck());
		accountCommandService.resetPassword(requestingAccountId, encodedPassword);
	}

	@Transactional
	public UploadProfileImageResponse saveProfileImage(UploadProfileImageRequest uploadProfileImageRequest,
		long requestedAccountId) throws IOException {
		byte[] fileBytes = convertMultiPartFileToFileBytes(uploadProfileImageRequest.getMultipartFile());

		String filename = UUID.randomUUID().toString();

		saveFileToS3(uploadProfileImageRequest, fileBytes, filename);

		String imageUrl = extractS3Url(filename);

		Account account = accountCommandService.updateImageUrl(requestedAccountId, imageUrl);

		return new UploadProfileImageResponse(account.getImageUrl());
	}

	private String extractS3Url(String filename) {
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;
	}

	private void saveFileToS3(UploadProfileImageRequest uploadProfileImageRequest, byte[] fileBytes,
		String filename) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(fileBytes.length);
		metadata.setContentType(uploadProfileImageRequest.getMultipartFile().getContentType());
		amazonS3.putObject(new PutObjectRequest(bucket, filename, new ByteArrayInputStream(fileBytes), metadata));
	}

	private byte[] convertMultiPartFileToFileBytes(MultipartFile multipartFile) throws IOException {
		return multipartFile.getBytes();
	}
}
