package org.hoongoin.interviewbank.scrap.infrastructure.repository;

import java.util.Optional;

import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapLikeRepository extends JpaRepository<ScrapLikeEntity, Long> {
	@Query("SELECT scrap_like FROM ScrapLikeEntity scrap_like "
		+ "WHERE scrap_like.accountEntity.id = :account_id "
		+ "AND scrap_like.scrapEntity.id = :scrap_id")
	Optional<ScrapLikeEntity> findByAccountIdAndInterviewId(@Param("account_id") long accountId, @Param("scrap_id") long scrapId);
}
