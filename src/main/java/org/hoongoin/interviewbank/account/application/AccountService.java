package org.hoongoin.interviewbank.account.application;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.controller.request.LoginRequest;
import org.hoongoin.interviewbank.account.controller.request.ModifyNicknameRequest;
import org.hoongoin.interviewbank.account.controller.request.RegisterRequest;
import org.hoongoin.interviewbank.account.controller.request.ResetPasswordRequest;
import org.hoongoin.interviewbank.account.controller.request.SendEmailRequest;
import org.hoongoin.interviewbank.account.controller.response.GetMeResponse;
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
import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbLoginFailedException;
import org.hoongoin.interviewbank.exception.IbValidationException;
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
		account.setPasswordUpdatedAt(LocalDateTime.now());

		account = accountCommandService.insertAccount(account);

		return accountMapper.accountToRegisterResponse(account);
	}

	public Account loginByLoginRequest(LoginRequest loginRequest) {
		Account account;
		try {
			account = accountQueryService.findAccountByEmailAndAccountType(loginRequest.getEmail(), AccountType.EMAIL);
		} catch (IbEntityNotFoundException e) {
			throw new IbLoginFailedException("Email or Password is not correct");
		}

		if (!passwordEncoder.matches(loginRequest.getPassword(), account.getPassword())) {
			throw new IbLoginFailedException("Email or Password is not correct");
		}

		return account;
	}

	public void createPasswordResetTokenAndSendEmailByRequest(SendEmailRequest sendEmailRequest) {
		Account account = accountQueryService.findAccountByEmailAndAccountType(sendEmailRequest.getEmail(),
			AccountType.EMAIL);

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
		long requestedAccountId) {
		byte[] fileBytes = resizeImageSizeOfFile(multipartFile);

		String filename = UUID.randomUUID().toString();

		saveFileToS3(multipartFile, fileBytes, filename);

		String imageUrl = extractS3Url(filename);

		Account account = accountCommandService.updateImageUrl(requestedAccountId, imageUrl);

		return new UploadProfileImageResponse(account.getImageUrl());
	}

	private byte[] resizeImageSizeOfFile(MultipartFile multipartFile) {
		BufferedImage originalImage;

		try {
			originalImage = ImageIO.read(multipartFile.getInputStream());
		} catch (IOException e) {
			throw new IbBadRequestException(e.getMessage());
		}

		int resizedWidth = resize(originalImage.getWidth());
		int resizedHeight = resize(originalImage.getHeight());

		BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, originalImage.getType());
		Graphics2D graphics = resizedImage.createGraphics();
		graphics.drawImage(originalImage, 0, 0, resizedWidth, resizedHeight, null);
		graphics.dispose();

		return imageAndContentTypeToBytes(resizedImage, multipartFile.getContentType());
	}

	private byte[] imageAndContentTypeToBytes(BufferedImage image, String contentType) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(image, getFormatName(contentType), bos);
			return bos.toByteArray();
		} catch (IOException e) {
			throw new IbBadRequestException(e.getMessage());
		}
	}

	private String getFormatName(String contentType) {
		return switch (contentType) {
			case "image/jpeg" -> "jpeg";
			case "image/png" -> "png";
			case "image/gif" -> "gif";
			default -> throw new IllegalArgumentException("Unsupported image type: " + contentType);
		};
	}

	private int resize(int size) {
		return Math.min(size, 400);
	}

	private String extractS3Url(String filename) {
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;
	}

	private void saveFileToS3(MultipartFile multipartFile, byte[] fileBytes,
		String filename) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(fileBytes.length);
		metadata.setContentType(multipartFile.getContentType());
		amazonS3.putObject(new PutObjectRequest(bucket, filename, new ByteArrayInputStream(fileBytes), metadata));
	}
}
