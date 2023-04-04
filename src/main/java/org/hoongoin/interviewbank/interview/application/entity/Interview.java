package org.hoongoin.interviewbank.interview.application.entity;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.exception.IbValidationException;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Interview {

	@Builder
	public Interview(Long interviewId, String title, Long accountId, LocalDateTime createdAt,
		LocalDateTime updatedAt, LocalDateTime deletedAt, Boolean deletedFlag, Long jobCategoryId,
		InterviewPeriod interviewPeriod, CareerYear careerYear) {
		this.setInterviewId(interviewId);
		this.setTitle(title);
		this.setAccountId(accountId);
		this.setCreatedAt(createdAt);
		this.setUpdatedAt(updatedAt);
		this.setDeletedAt(deletedAt);
		this.setDeletedFlag(deletedFlag);
		this.setJobCategoryId(jobCategoryId);
		this.setInterviewPeriod(interviewPeriod);
		this.setCareerYear(careerYear);
	}

	private Long interviewId;
	private String title;
	private Long accountId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private Boolean deletedFlag;
	private Long jobCategoryId;
	private CareerYear careerYear;
	private InterviewPeriod interviewPeriod;

	public void setTitle(String title) {
		if (validateTitle(title)) {
			throw new IbValidationException("title");
		}
		this.title = title;
	}

	private boolean validateTitle(String title) {
		return title.getBytes().length > 128 || title.getBytes().length < 1 || title.isEmpty();
	}
}
