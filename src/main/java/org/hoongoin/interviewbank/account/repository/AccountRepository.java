package org.hoongoin.interviewbank.account.repository;

import java.util.Optional;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

	boolean existsByEmail(String email);

	Optional<AccountEntity> findByEmail(String email);
}
