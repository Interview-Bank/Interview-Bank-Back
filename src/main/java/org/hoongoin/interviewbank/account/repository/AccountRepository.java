package org.hoongoin.interviewbank.account.repository;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
