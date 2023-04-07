package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.AccountMapper;
import org.hoongoin.interviewbank.account.application.dto.KakaoUerInfoResponse;
import org.hoongoin.interviewbank.account.controller.request.ModifyNicknameRequest;
import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.account.infrastructure.repository.AccountRepository;
import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbBadRequestException;
import org.hoongoin.interviewbank.exception.IbEntityExistsException;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

	private final AccountRepository accountRepository;
	private final AccountMapper accountMapper;
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;
	@Value("${aws.region.static}")
	private String region;

	public Account insertAccount(Account account) {
		Boolean emailExists = accountRepository.existsByEmailAndAccountType(account.getEmail(), AccountType.EMAIL);
		if (Boolean.TRUE.equals(emailExists)) {
			throw new IbEntityExistsException(account.getEmail());
		}
		AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
		accountRepository.save(accountEntity);

		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public void resetPassword(long requestingAccountId, String password) {
		AccountEntity accountEntity = accountRepository.findById(requestingAccountId)
			.orElseThrow(() -> new IbEntityNotFoundException("Account"));

		accountEntity.resetPassword(password);
	}

	@Transactional
	public Account insertIfNotExists(Account account) {
		Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
			account.getEmail(), account.getAccountType());
		if (optionalAccountEntity.isEmpty()) {
			AccountEntity accountEntity = accountMapper.accountToAccountEntity(account);
			accountRepository.save(accountEntity);
			account = accountMapper.accountEntityToAccount(accountEntity);
		} else {
			account = accountMapper.accountEntityToAccount(optionalAccountEntity.get());
		}
		return account;
	}

	@Transactional
	public Account saveKakaoUserIfNotExists(KakaoUerInfoResponse kakaoUserInfoResponse) {
		Optional<AccountEntity> optionalAccountEntity = accountRepository.findByEmailAndAccountType(
			kakaoUserInfoResponse.getKakao_account().getEmail(), AccountType.KAKAO);
		AccountEntity accountEntity;
		if (optionalAccountEntity.isPresent()) {
			accountEntity = optionalAccountEntity.get();
		} else {
			accountEntity = AccountEntity.builder()
				.nickname(kakaoUserInfoResponse.getKakao_account().getProfile().getNickname())
				.email(kakaoUserInfoResponse.getKakao_account().getEmail())
				.accountType(AccountType.KAKAO)
				.imageUrl(kakaoUserInfoResponse.getProperties().profile_image)
				.build();
			accountRepository.save(accountEntity);
		}
		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account modifyNickname(ModifyNicknameRequest modifyNicknameRequest, long requestingAccountId) {
		AccountEntity accountEntity = accountRepository.findById(requestingAccountId)
			.orElseThrow(() -> new IbEntityNotFoundException("Account"));
		accountEntity.editNickname(modifyNicknameRequest.getNickname());
		return accountMapper.accountEntityToAccount(accountEntity);
	}

	public Account updateImageUrl(long requestedAccountId, MultipartFile multipartFile) {
		AccountEntity accountEntity = accountRepository.findById(requestedAccountId)
			.orElseThrow(() -> new IbEntityNotFoundException("Account"));

		checkImageUrlOfAccountEntity(accountEntity);

		byte[] fileBytes = resizeImageSizeOfFile(multipartFile);

		String filename = UUID.randomUUID().toString();

		saveFileToS3(multipartFile, fileBytes, filename);

		String extractedS3Url = extractS3Url(filename);

		accountEntity.uploadImageUrl(extractedS3Url);

		return accountMapper.accountEntityToAccount(accountEntity);
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

	private int resize(int size) {
		return Math.min(size, 400);
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

	private void saveFileToS3(MultipartFile multipartFile, byte[] fileBytes,
		String filename) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(fileBytes.length);
		metadata.setContentType(multipartFile.getContentType());
		amazonS3.putObject(new PutObjectRequest(bucket, filename, new ByteArrayInputStream(fileBytes), metadata));
	}

	private String extractS3Url(String filename) {
		return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + filename;
	}

	private void checkImageUrlOfAccountEntity(AccountEntity accountEntity) {
		if (!accountEntity.getImageUrl().isEmpty()) {
			amazonS3.deleteObject(new DeleteObjectRequest(bucket, getObjectKeyFromS3Url(accountEntity.getImageUrl())));
		}
	}

	public String getObjectKeyFromS3Url(String url) {
		try {
			URI uri = new URI(url);
			return uri.getPath().substring(1);
		} catch (Exception e) {
			throw new IbValidationException("invalid uri format");
		}
	}
}
