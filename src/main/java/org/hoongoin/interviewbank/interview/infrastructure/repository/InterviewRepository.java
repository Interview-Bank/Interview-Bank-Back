package org.hoongoin.interviewbank.interview.infrastructure.repository;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview ORDER BY interview.createdAt ASC")
	Page<InterviewEntity> findAllByPageableOrderByCreateTimeAsc(Pageable pageable);

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview " 
		+ "WHERE interview.jobCategoryEntity.id = :job_category_id " 
		+ "AND interview.title LIKE %:query%" 
		+ " ORDER BY interview.createdAt ASC")
	Page<InterviewEntity> findAllByTitleAndJobCategoryIdAndPageableOrderByCreateTimeAsc(@Param("query") String query,
		@Param("job_category_id") long jobCategoryId, PageRequest of);

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview "
		+ "WHERE interview.title LIKE %:query%"
		+ " ORDER BY interview.createdAt ASC")
	Page<InterviewEntity> findAllByTitleAndPageableOrderByCreateTimeAsc(@Param("query") String query, PageRequest of);
}
