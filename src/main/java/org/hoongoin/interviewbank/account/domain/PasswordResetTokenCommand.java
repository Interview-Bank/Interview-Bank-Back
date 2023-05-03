package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.hoongoin.interviewbank.account.infrastructure.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenCommand {

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	public void saveToken(long accountId, String email, String token) {
		PasswordResetToken passwordResetToken = new PasswordResetToken(accountId, email, token);
		passwordResetTokenRepository.save(passwordResetToken);
	}
}
