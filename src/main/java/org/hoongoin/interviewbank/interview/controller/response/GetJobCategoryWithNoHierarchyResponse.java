package org.hoongoin.interviewbank.interview.controller.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetJobCategoryWithNoHierarchyResponse {
	private long jobCategoryId;
	private String name;
}
