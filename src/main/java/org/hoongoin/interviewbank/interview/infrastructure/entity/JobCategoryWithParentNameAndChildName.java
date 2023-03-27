package org.hoongoin.interviewbank.interview.infrastructure.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JobCategoryWithParentNameAndChildName {

	private long jobCategoryId;
	private String primaryJobCategory;
	private String secondaryJobCategory;
}
