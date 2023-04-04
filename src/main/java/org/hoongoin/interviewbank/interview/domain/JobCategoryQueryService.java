package org.hoongoin.interviewbank.interview.domain;

import java.util.List;

import org.hoongoin.interviewbank.interview.infrastructure.entity.FullJobCategory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;

public interface JobCategoryQueryService {

	public Long findJobCategoryIdByJobCategoryName(String primaryJobCategory, String secondaryJobCategory);

	public JobCategoryEntity findJobCategoryEntityByJobCategory(String primaryJobCategory, String secondaryJobCategory);

	public List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy();

	JobCategoryEntity findJobCategoryEntityById(Long jobCategoryId);

	FullJobCategory findFullJobCategoryById(Long jobCategoryId);
}
