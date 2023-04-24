package org.hoongoin.interviewbank.account.application;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.hoongoin.interviewbank.account.application.entity.Account;
import org.hoongoin.interviewbank.exception.IbInternalServerException;
import org.hoongoin.interviewbank.exception.IbValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountExternalService {

	private final MailService mailService;
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucket;
	@Value("${aws.region.static}")
	private String region;

	public void sendMailTo(String email, String hashedToken) {
		mailService.sendMailTo(email, hashedToken);
	}

	public String uploadImageFile(MultipartFile multipartFile) {
		byte[] fileBytes = resizeImageSizeOfFile(multipartFile);

		String filename = UUID.randomUUID().toString();

		saveFileToS3(multipartFile, fileBytes, filename);

		return extractS3Url(filename);
	}

	private byte[] resizeImageSizeOfFile(MultipartFile multipartFile) {
		BufferedImage originalImage;

		try {
			originalImage = ImageIO.read(multipartFile.getInputStream());
		} catch (IOException e) {
			log.info(e.getMessage());
			throw new IbInternalServerException("Internal Server Error");
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
			log.info(e.getMessage());
			throw new IbInternalServerException("Internal Server Error");
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

	public void checkImageUrlOfAccount(Account account) {
		if (account.getImageUrl() != null) {
			amazonS3.deleteObject(new DeleteObjectRequest(bucket, getObjectKeyFromS3Url(account.getImageUrl())));
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
