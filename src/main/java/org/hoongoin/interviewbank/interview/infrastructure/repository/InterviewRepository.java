package org.hoongoin.interviewbank.interview.infrastructure.repository;

import java.util.Date;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {

	@Query("SELECT interview FROM InterviewEntity interview ORDER BY interview.createdAt DESC")
	Page<InterviewEntity> findAllByPageableOrderByCreateTimeDesc(Pageable pageable);

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview "
		+ "WHERE (:job_category_id IS NULL OR interview.jobCategoryEntity.id = :job_category_id ) "
		+ "AND (:query IS NULL OR interview.title LIKE %:query%) "
		+ "AND (:start_date IS NULL OR interview.createdAt >= TIMESTAMP(:start_date)) "
		+ "AND (:end_date IS NULL OR interview.createdAt <= TIMESTAMP(:end_date)) "
		+ "ORDER BY interview.createdAt DESC")
	Page<InterviewEntity> findAllByTitleAndJobCategoryIdAndStartDateAndEndDatePageableOrderByCreateTimeAsc(
		@Param("query") String query, @Param("job_category_id") Long jobCategoryId, @Param("start_date") Date startDate,
		@Param("end_date") Date endDate, Pageable pageable);
}
