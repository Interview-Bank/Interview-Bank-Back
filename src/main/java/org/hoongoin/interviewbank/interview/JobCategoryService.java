package org.hoongoin.interviewbank.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hoongoin.interviewbank.interview.application.entity.JobCategoryWithNoHierarchy;
import org.hoongoin.interviewbank.interview.controller.response.GetJobCategoryResponse;
import org.hoongoin.interviewbank.interview.controller.response.GetJobCategoryWithNoHierarchyResponse;
import org.hoongoin.interviewbank.interview.domain.JobCategoryQueryService;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryWithHierarchy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobCategoryService {

	private final JobCategoryQueryService jobCategoryQueryService;
	private final InterviewMapper interviewMapper;

	public List<GetJobCategoryResponse> getJobCategory() {
		List<JobCategoryWithHierarchy> jobCategoryWithHierarchies = jobCategoryQueryService.findAllJobCategoriesWithHierarchy();
		List<GetJobCategoryResponse> getJobCategoryResponses = new ArrayList<>();
		Map<Long, GetJobCategoryResponse> responseMap = new HashMap<>();

		for (JobCategoryWithHierarchy dto : jobCategoryWithHierarchies) {
			GetJobCategoryResponse response = responseMap.get(dto.getFirstLevelId());
			if (response == null) {
				response = new GetJobCategoryResponse();
				response.setFirstLevelId(dto.getFirstLevelId());
				response.setFirstLevelName(dto.getFirstLevelName());
				response.setSecondJobCategories(new ArrayList<>());
				responseMap.put(dto.getFirstLevelId(), response);
				getJobCategoryResponses.add(response);
			}

			if (dto.getSecondLevelId() != null) {
				GetJobCategoryResponse.SecondJobCategory secondJobCategory = new GetJobCategoryResponse.SecondJobCategory();
				secondJobCategory.setSecondLevelId(dto.getSecondLevelId());
				secondJobCategory.setSecondLevelName(dto.getSecondLevelName());
				response.getSecondJobCategories().add(secondJobCategory);
			}
		}

		return getJobCategoryResponses;

	}

	public List<GetJobCategoryWithNoHierarchyResponse> getJobCategoryWithNoHierarchy() {
		List<JobCategoryWithNoHierarchy> jobCategoryWithNoHierarchies = jobCategoryQueryService.findAllJobCategories();
		List<GetJobCategoryWithNoHierarchyResponse> getJobCategoryWithNoHierarchyResponses = new ArrayList<>();
		jobCategoryWithNoHierarchies.forEach(
			jobCategoryWithNoHierarchy -> getJobCategoryWithNoHierarchyResponses.add(
				interviewMapper.jobCategoryWithNoHierarchyToGetJobCategoryWithNoHierarchyResponse(jobCategoryWithNoHierarchy))
		);
		return getJobCategoryWithNoHierarchyResponses;
	}
}
