package org.hoongoin.interviewbank.interview.domain;

import java.util.List;

import org.hoongoin.interviewbank.exception.IbEntityNotFoundException;
import org.hoongoin.interviewbank.interview.application.entity.JobCategory;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.interview.infrastructure.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobCategoryQueryServiceImplementation implements JobCategoryQueryService {

	private final JobCategoryRepository jobCategoryRepository;

	@Override
	public List<JobCategoryWithHierarchy> findAllJobCategoriesWithHierarchy() {
		return jobCategoryRepository.findAllJobCategoriesWithHierarchy();
	}

	@Override
	public JobCategoryEntity findJobCategoryEntityById(Long jobCategoryId) {
		return jobCategoryRepository.findById(jobCategoryId)
			.orElseThrow(() -> {
				log.info("Job Category Not Found");
				return new IbEntityNotFoundException("Job Category Not Found");
			});
	}

	@Override
	public JobCategory findJobCategoryById(long jobCategoryId) {
		return jobCategoryRepository.findFullJobCategoryById(jobCategoryId)
			.orElseThrow(() -> {
				log.info("Job Category Not Found");
				return new IbEntityNotFoundException("Job Category Not Found");
			});
	}
}
