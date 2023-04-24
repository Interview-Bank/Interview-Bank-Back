package org.hoongoin.interviewbank.account.domain;

import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.hoongoin.interviewbank.account.infrastructure.repository.PasswordResetTokenRepository;
import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordResetTokenQuery {

	private final PasswordResetTokenRepository passwordResetTokenRepository;

	public boolean existsPasswordResetTokenByHashedToken(String hashedToken) {
		return passwordResetTokenRepository.existsByToken(hashedToken);
	}

	public PasswordResetToken findResetTokenByToken(String token) {
		return passwordResetTokenRepository.findById(token)
			.orElseThrow(() -> {
				log.info("Password Reset Token Not Found");
				return new IbEntityNotFoundException("Password Reset Token Not Found");
			});
	}
}
