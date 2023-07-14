package org.hoongoin.interviewbank.interview.domain;

import java.util.ArrayList;
import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.application.entity.JobCategoryWithNoHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobCategoryQueryService {

	private final JobCategoryRepository jobCategoryRepository;

	public List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy() {
		return jobCategoryRepository.findAllJobCategoriesWithHierarchy();
	}

	public List<JobCategoryWithNoHierarchy> findAllJobCategories(){
		List<JobCategoryEntity> jobCategoryEntities = jobCategoryRepository.findAll();
		List<JobCategoryWithNoHierarchy> jobCategoryWithNoHierarchies = new ArrayList<>();
		jobCategoryEntities.forEach(jobCategoryEntity ->
			jobCategoryWithNoHierarchies.add(new JobCategoryWithNoHierarchy(jobCategoryEntity.getId(), jobCategoryEntity.getName()))
		);
		return jobCategoryWithNoHierarchies;
	}

	public JobCategoryEntity findJobCategoryEntityById(Long jobCategoryId) {
		return jobCategoryRepository.findById(jobCategoryId)
			.orElseThrow(() -> {
				log.info("Job Category Not Found");
				return new IbEntityNotFoundException("Job Category Not Found");
			});
	}

	public JobCategory findJobCategoryById(long jobCategoryId) {
		return jobCategoryRepository.findFullJobCategoryById(jobCategoryId)
			.orElseThrow(() -> {
				log.info("Job Category Not Found");
				return new IbEntityNotFoundException("Job Category Not Found");
			});
	}
}
