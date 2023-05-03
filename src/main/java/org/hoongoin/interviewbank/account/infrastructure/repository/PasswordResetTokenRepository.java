package org.hoongoin.interviewbank.account.infrastructure.repository;

import java.util.Optional;

import org.hoongoin.interviewbank.account.infrastructure.entity.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;

public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, String> {

	Optional<PasswordResetToken> findByToken(String token);

	boolean existsByToken(String hashedToken);
}
