package org.hoongoin.interviewbank.scrap.controller.response;

import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadScrapPageResponse {

	private int totalPages;
	private long totalElements;
	private List<Scrap> scraps;

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Scrap {
		private long scrapId;
		private String title;
		private JobCategoryResponse jobCategory;
		private String nickname;
		private LocalDate createdAt;
	}
}
