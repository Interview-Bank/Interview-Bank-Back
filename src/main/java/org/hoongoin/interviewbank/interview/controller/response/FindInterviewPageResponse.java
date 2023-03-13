package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FindInterviewPageResponse {

	private List<Interview> interviews;

	@Getter
	@AllArgsConstructor
	public static class Interview {

		private Long interviewId;
		private String nickname;
		private LocalDateTime createdAt;
		private String title;
	}
}