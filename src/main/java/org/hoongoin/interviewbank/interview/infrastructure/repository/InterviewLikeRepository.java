package org.hoongoin.interviewbank.interview.infrastructure.repository;

import java.util.Optional;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewLikeRepository extends JpaRepository<InterviewLikeEntity, Long> {
	@Query("select (count(interview_like) > 0) from InterviewLikeEntity interview_like "
		+ "where interview_like.accountEntity.id = :account_id and interview_like.interviewEntity.id = :interview_id")
	boolean existsByAccountIdAndInterviewId(@Param("account_id") long accountId, @Param("interview_id") long interviewId);

	@Query("SELECT interview_like FROM InterviewLikeEntity interview_like "
		+ "WHERE interview_like.accountEntity.id = :account_id "
		+ "AND interview_like.interviewEntity.id = :interview_id")
	Optional<InterviewLikeEntity> findByAccountIdAndInterviewId(@Param("account_id") long accountId, @Param("interview_id") long interviewId);
}
