package org.hoongoin.interviewbank.scrap.controller.response;

import org.hoongoin.interviewbank.interview.controller.response.JobCategoryResponse;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadScrapResponse {

	private long scrapId;
	private String title;
	private JobCategoryResponse jobCategory;
	private String nickname;
	private LocalDate createdAt;
}
