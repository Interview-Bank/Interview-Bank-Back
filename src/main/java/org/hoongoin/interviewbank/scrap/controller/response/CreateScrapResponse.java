package org.hoongoin.interviewbank.scrap.controller.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CreateScrapResponse {

	private OriginalInterviewResponse originalInterview;
	private ScrapResponse scrap;
	private List<ScrapQuestionAndScrapAnswerResponse> scrapQuestionAndScrapAnswerList;

	@AllArgsConstructor
	@Getter
	@Setter
	public static class OriginalInterviewResponse {

		private long interviewId;
		private String title;
	}

	@AllArgsConstructor
	@Getter
	@Setter
	public static class ScrapResponse {

		private long scrapId;
		private String title;
		private LocalDate createdAt;
	}

	@Getter
	@AllArgsConstructor
	public static class ScrapQuestionAndScrapAnswerResponse {

		private ScrapQuestionResponse scrapQuestion;
		private ScrapAnswerResponse scrapAnswer;

		@AllArgsConstructor
		@Getter
		@Setter
		public static class ScrapQuestionResponse {

			private long scrapQuestionId;
			private String content;
			private String gptAnswer;
		}

		@Getter
		@AllArgsConstructor
		public static class ScrapAnswerResponse {

			private long scrapAnswerId;
			private String content;
		}
	}
}
