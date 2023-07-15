package org.hoongoin.interviewbank.interview.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobCategoryRepository extends JpaRepository<JobCategoryEntity, Long> {

	@Query("SELECT new org.hoongoin.interviewba₩nk.interview.infrastructure.entity.JobCategoryWithHierarchy(" +
		"firstLevel.id, firstLevel.name, secondLevel.id, secondLevel.name) " +
		"FROM JobCategoryEntity firstLevel " +
		"LEFT JOIN JobCategoryEntity secondLevel ON firstLevel.id = secondLevel.parentJobCategory.id " +
		"WHERE firstLevel.parentJobCategory IS NULL")
	List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy();

	@Query("SELECT new org.hoongoin.interviewbank.interview.application.entity.JobCategory(jc.id, " +
		"COALESCE(pjc.name, jc.name), " +
		"CASE WHEN pjc.name IS NULL THEN NULL ELSE jc.name END) " +
		"FROM JobCategoryEntity jc " +
		"LEFT JOIN jc.parentJobCategory pjc " +
		"WHERE jc.id = :job_category_id")
	Optional<JobCategory> findFullJobCategoryById(@Param("job_category_id") long jobCategoryId);
}
