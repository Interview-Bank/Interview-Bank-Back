package org.hoongoin.interviewbank.interview.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindMyInterviewResponse {

	private List<Interview> interviews;

	@Getter
	@AllArgsConstructor
	public static class Interview {

		private Long interviewId;
		private String nickname;
		private LocalDateTime createdAt;
		private String title;
		private InterviewPeriod interviewPeriod;
		private CareerYear careerYear;
	}
}
