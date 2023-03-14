package org.hoongoin.interviewbank.interview.infrastructure.repository;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview ORDER BY interview.createdAt ASC")
	Page<InterviewEntity> findAllByPageableOrderByCreateTimeAsc(Pageable pageable);
}
