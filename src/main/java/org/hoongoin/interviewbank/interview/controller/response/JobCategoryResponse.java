package org.hoongoin.interviewbank.interview.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobCategoryResponse {

	private Long jobCategoryId;
	private String firstLevelName;
	private String secondLevelName;
}
