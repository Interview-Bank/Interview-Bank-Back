package org.hoongoin.interviewbank.interview.controller;

import java.util.List;

import org.hoongoin.interviewbank.interview.JobCategoryService;
import org.hoongoin.interviewbank.interview.controller.response.GetJobCategoryResponse;
import org.hoongoin.interviewbank.interview.controller.response.GetJobCategoryWithNoHierarchyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("job-categories")
@RequiredArgsConstructor
public class JobCategoryController {

	private final JobCategoryService jobCategoryService;

	@GetMapping
	public ResponseEntity<List<GetJobCategoryResponse>> getJobCategory() {
		return ResponseEntity.ok(jobCategoryService.getJobCategory());
	}

	@GetMapping("/no-hierarchy")
	public ResponseEntity<List<GetJobCategoryWithNoHierarchyResponse>> getJobCategoryWithNoHierarchy(){
		return ResponseEntity.ok(jobCategoryService.getJobCategoryWithNoHierarchy());
	}
}
