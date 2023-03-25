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

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview ORDER BY interview.createdAt ASC")
	Page<InterviewEntity> findAllByPageableOrderByCreateTimeAsc(Pageable pageable);

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview "
		+ "WHERE (interview.jobCategoryEntity.id = :job_category_id  OR :job_category_id IS NULL) "
		+ "AND (interview.title LIKE %:query% OR :query IS NULL) "
		+ "AND (interview.createdAt >= :start_date OR :start_date IS NULL) "
		+ "AND (interview.createdAt <= :end_date OR :end_date IS NULL) "
		+ "ORDER BY interview.createdAt DESC")
	Page<InterviewEntity> findAllByTitleAndJobCategoryIdAndStartDateAndEndDatePageableOrderByCreateTimeAsc(
		@Param("query") String query, @Param("job_category_id") Long jobCategoryId, @Param("start_date") Date startDate,
		@Param("end_date") Date endDate, Pageable pageable);
}
