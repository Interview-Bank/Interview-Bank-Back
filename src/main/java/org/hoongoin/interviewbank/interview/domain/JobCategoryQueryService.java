package org.hoongoin.interviewbank.interview.domain;

import java.util.List;

import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;

public interface JobCategoryQueryService {

	public Long findJobCategoryIdByJobCategoryName(String primaryJobCategory, String secondaryJobCategory);

	public JobCategoryEntity findJobCategoryEntityByJobCategory(String primaryJobCategory, String secondaryJobCategory);

	public List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy();

	JobCategoryEntity findJobCategoryEntityById(Long jobCategoryId);

	JobCategory findJobCategoryById(long jobCategoryId);
}
