package org.hoongoin.interviewbank.scrap.infrastructure.repository;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends JpaRepository<ScrapEntity, Long> {
	Page<ScrapEntity> findByInterviewEntityAndIsPublic(InterviewEntity interviewEntity, boolean isPublic,
		Pageable pageable);

	@Query("SELECT count(scrap) > 0 FROM ScrapEntity scrap "
		+ "WHERE scrap.interviewEntity.id = :interview_id "
		+ "and scrap.accountEntity.id = :account_id")
	boolean existsByInterviewIdAndAccountId(@Param("interview_id") Long interviewId,
		@Param("account_id") Long accountId);

	@Query("SELECT scrap FROM ScrapEntity scrap "
		+ "WHERE scrap.accountEntity.id = :account_id "
		+ "ORDER BY scrap.createdAt DESC")
	Page<ScrapEntity> findByAccountIdAndPageableOrderByCreatedAtDesc(@Param("account_id") long accountId,
		Pageable pageable);


}
