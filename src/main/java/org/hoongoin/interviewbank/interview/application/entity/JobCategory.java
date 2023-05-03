package org.hoongoin.interviewbank.interview.application.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JobCategory {

	private long jobCategoryId;
	private String firstLevelName;
	private String secondLevelName;
}
