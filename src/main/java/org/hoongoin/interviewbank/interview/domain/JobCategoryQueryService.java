package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.springframework.stereotype.Service;

@Service
public interface JobCategoryQueryService {

	public Long findJobCategoryIdByJobCategoryName(String primaryJobCategory, String secondaryJobCategory);

	public JobCategoryEntity findJobCategoryEntityByJobCategory(String primaryJobCategory, String secondaryJobCategory);
}
