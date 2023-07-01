package org.hoongoin.interviewbank.scrap.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ReadScrapDetailResponse {

	private ScrapResponse scrap;
	private OriginalInterviewResponse originalInterview;
	private List<ScrapQuestionWithScrapAnswersResponse> scrapQuestionWithScrapAnswersList;

	@Builder
	@Getter
	@Setter
	public static class ScrapResponse {

		private long scrapId;
		private String title;
		private LocalDateTime createdAt;
		private String writerNickname;
		private long writerAccountId;
		private long view;
	}

	@AllArgsConstructor
	@Getter
	@Setter
	public static class OriginalInterviewResponse {

		private long interviewId;
		private String title;
	}


	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	@Setter
	public static class ScrapQuestionWithScrapAnswersResponse {

		private long scrapQuestionId;
		private String content;
		private String gptAnswer;
		private List<ScrapAnswerResponse> scrapAnswerResponseList;

		@Getter
		@AllArgsConstructor
		public static class ScrapAnswerResponse {
			private long scrapAnswerId;
			private String content;
		}
	}
}
