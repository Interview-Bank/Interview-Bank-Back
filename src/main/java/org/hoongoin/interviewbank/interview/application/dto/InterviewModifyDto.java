package org.hoongoin.interviewbank.interview.application.dto;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InterviewModifyDto {

	private final String title;
	private final InterviewPeriod interviewPeriod;
	private final CareerYear careerYear;
	private final JobCategoryEntity jobCategoryEntity;
}
