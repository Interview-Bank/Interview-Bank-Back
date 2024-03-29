package org.hoongoin.interviewbank.interview.infrastructure.repository;

import static org.hoongoin.interviewbank.interview.infrastructure.entity.QInterviewEntity.*;

import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long>,
	QuerydslPredicateExecutor<InterviewEntity> {

	@Query("SELECT interview FROM InterviewEntity interview ORDER BY interview.createdAt DESC")
	Page<InterviewEntity> findAllByPageableOrderByCreateTimeDesc(Pageable pageable);

	@EntityGraph(attributePaths = {"accountEntity"})
	@Query("SELECT interview FROM InterviewEntity interview "
		+ "WHERE (COALESCE(:job_category_ids, NULL) IS NULL OR interview.jobCategoryEntity.id IN :job_category_ids OR interview.jobCategoryEntity.parentJobCategory.id IN :job_category_ids) "
		+ "AND (:query IS NULL OR interview.title LIKE %:query%) "
		+ "AND (:start_date IS NULL OR interview.createdAt >= TIMESTAMP(:start_date)) "
		+ "AND (:end_date IS NULL OR interview.createdAt <= TIMESTAMP(:end_date)) "
		+ "AND (:interview_period IS NULL OR interview.interviewPeriod = :interview_period) "
		+ "AND (:career_year IS NULL OR interview.careerYear = :career_year) "
		+ "ORDER BY interview.createdAt DESC")
	Page<InterviewEntity> findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
		@Param("query") String query, @Param("job_category_ids") List<Long> jobCategoryIds, @Param("start_date") Date startDate,
		@Param("end_date") Date endDate, @Param("interview_period") InterviewPeriod interviewPeriod,
		@Param("career_year") CareerYear careerYear, Pageable pageable);

	default Page<InterviewEntity> findByAccountEntityIdAndDeleteFlag(Pageable pageable, long accountId) {
		BooleanExpression hasAccountId = interviewEntity.accountEntity.id.eq(accountId);
		BooleanExpression hasDeleteFlag = interviewEntity.deletedFlag.eq(Boolean.FALSE);
		return findAll(hasAccountId.and(hasDeleteFlag), pageable);
	}
}
