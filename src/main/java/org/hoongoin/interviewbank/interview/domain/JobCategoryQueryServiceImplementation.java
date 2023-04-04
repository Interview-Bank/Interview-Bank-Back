package org.hoongoin.interviewbank.interview.domain;

import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.infrastructure.entity.FullJobCategory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JobCategoryQueryServiceImplementation implements JobCategoryQueryService{

	private final JobCategoryRepository jobCategoryRepository;

	@Override
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

	@Override
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

	@Override
	public List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy() {
		return jobCategoryRepository.findAllJobCategoriesWithHierarchy();
	}

	@Override
	public JobCategoryEntity findJobCategoryEntityById(Long jobCategoryId){
		return jobCategoryRepository.findById(jobCategoryId)
				.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
	}

	@Override
	public FullJobCategory findFullJobCategoryById(Long jobCategoryId){
		return jobCategoryRepository.findFullJobCategoryById(jobCategoryId)
			.orElseThrow(() -> new IbEntityNotFoundException("Job Category"));
	}
}
