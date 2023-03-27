package org.hoongoin.interviewbank.interview.domain;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JobCategoryQueryServiceImplementation implements JobCategoryQueryService{

	private final JobCategoryRepository jobCategoryRepository;

	public Long findJobCategoryIdByJobCategoryName(String primaryJobCategory, String secondaryJobCategory) {
		Long jobCategoryId = null;
		if (primaryJobCategory != null && secondaryJobCategory != null) {
			JobCategoryEntity jobCategoryEntity = jobCategoryRepository.findByNameAndParentJobCategoryName(
					secondaryJobCategory, primaryJobCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
			jobCategoryId = jobCategoryEntity.getId();
		} else if (primaryJobCategory != null) {
			JobCategoryEntity jobCategoryEntity = jobCategoryRepository.findByName(primaryJobCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
			jobCategoryId = jobCategoryEntity.getId();
		}
		return jobCategoryId;
	}

	public JobCategoryEntity findJobCategoryEntityByJobCategory(String primaryCategory, String secondaryCategory) {
		JobCategoryEntity jobCategoryEntity;
		if (primaryCategory == null){
			jobCategoryEntity = null;
		}
		else if (secondaryCategory == null) {
			jobCategoryEntity = jobCategoryRepository.findByName(primaryCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
		}
		else{
			jobCategoryEntity = jobCategoryRepository.findByNameAndParentJobCategoryName(
					secondaryCategory, primaryCategory)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
		}
		return jobCategoryEntity;
	}
}
