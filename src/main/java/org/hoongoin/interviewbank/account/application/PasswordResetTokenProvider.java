package org.hoongoin.interviewbank.account.application;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenProvider {

	private final MessageDigest messageDigest;
	private final Encoder base64Encoder;

	@Autowired
	private PasswordResetTokenProvider() throws NoSuchAlgorithmException {
		this.messageDigest = MessageDigest.getInstance("SHA-256");
		this.base64Encoder = Base64.getEncoder();
	}

	public String createToken() {
		UUID uuid = UUID.randomUUID();
		String token = uuid.toString();
		return hashToken(token);
	}

	private String hashToken(String token) {
		byte[] hashedTokenBytes = messageDigest.digest(token.getBytes(StandardCharsets.UTF_8));
		return base64Encoder.encodeToString(hashedTokenBytes);
	}
}
