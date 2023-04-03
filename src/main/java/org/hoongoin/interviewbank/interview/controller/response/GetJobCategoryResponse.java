package org.hoongoin.interviewbank.interview.controller.response;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GetJobCategoryResponse {

	private Long firstLevelId;
	private String firstLevelName;
	private List<SecondJobCategory> secondJobCategories;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class SecondJobCategory {
		private Long secondLevelId;
		private String secondLevelName;
	}
}
