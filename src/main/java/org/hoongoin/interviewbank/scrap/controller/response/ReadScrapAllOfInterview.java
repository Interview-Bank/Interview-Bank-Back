package org.hoongoin.interviewbank.scrap.controller.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadScrapAllOfInterview {

	private int totalPages;
	private long totalElements;
	private int currentPage;
	private int currentElements;
	private List<ReadScrapAllOfInterview.Scrap> scraps;

	@Getter
	@Builder
	@AllArgsConstructor
	public static class Scrap {
		private long scrapId;
		private String title;
		private String nickname;
		private LocalDate createdAt;
		private boolean like;
	}
}
