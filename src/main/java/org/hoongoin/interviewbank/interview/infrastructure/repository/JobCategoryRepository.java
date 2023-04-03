package org.hoongoin.interviewbank.interview.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobCategoryRepository extends JpaRepository<JobCategoryEntity, Long> {
	@Query("SELECT jc1 FROM JobCategoryEntity jc1 LEFT JOIN JobCategoryEntity jc2 "
		+ "ON jc1.parentJobCategory.id = jc2.id "
		+ "WHERE jc1.name = :name "
		+ "AND jc2.name = :parent_name")
	Optional<JobCategoryEntity> findByNameAndParentJobCategoryName(@Param("name") String name,
		@Param("parent_name") String parentJobCategoryName);

	Optional<JobCategoryEntity> findByName(String name);

	@Query("SELECT new org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy(" +
		"firstLevel.id, firstLevel.name, secondLevel.id, secondLevel.name) " +
		"FROM JobCategoryEntity firstLevel " +
		"LEFT JOIN JobCategoryEntity secondLevel ON firstLevel.id = secondLevel.parentJobCategory.id " +
		"WHERE firstLevel.parentJobCategory IS NULL")
	List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy();
}
