package org.hoongoin.interviewbank.interview.application.entity;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@ToString
public class TemporaryInterview {

	@Builder
	public TemporaryInterview(Long temporaryInterviewId, String title, Long accountId, LocalDateTime createdAt,
		LocalDateTime updatedAt, Long jobCategoryId,
		InterviewPeriod interviewPeriod, CareerYear careerYear) {
		this.setTemporaryInterviewId(temporaryInterviewId);
		this.setTitle(title);
		this.setAccountId(accountId);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setJobCategoryId(jobCategoryId);
		this.setInterviewPeriod(interviewPeriod);
		this.setCareerYear(careerYear);
	}

	private Long temporaryInterviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long jobCategoryId;
	private CareerYear careerYear;
	private InterviewPeriod interviewPeriod;
}
