package org.hoongoin.interviewbank.interview.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FullJobCategory {

	private Long jobCategoryId;
	private String firstLevelName;
	private String secondLevelName;
}
