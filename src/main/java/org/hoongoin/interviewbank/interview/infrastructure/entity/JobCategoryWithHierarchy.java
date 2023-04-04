package org.hoongoin.interviewbank.interview.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobCategoryWithHierarchy {

	private Long firstLevelId;
	private String firstLevelName;
	private Long secondLevelId;
	private String secondLevelName;
}
