package org.hoongoin.interviewbank.interview.infrastructure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.common.entity.BaseEntity;
import org.hoongoin.interviewbank.interview.application.dto.InterviewModifyDto;
import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "temporary_interview")
public class TemporaryInterviewEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 128)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "account_id")
	private AccountEntity accountEntity;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "job_category_id")
	private JobCategoryEntity jobCategoryEntity;

	@Enumerated(EnumType.STRING)
	private InterviewPeriod interviewPeriod;

	@Enumerated(EnumType.STRING)
	private CareerYear careerYear;

	public void modifyEntity(InterviewModifyDto interviewModifyDto) {
		this.title = interviewModifyDto.getTitle();
		this.interviewPeriod = interviewModifyDto.getInterviewPeriod();
		this.careerYear = interviewModifyDto.getCareerYear();
		this.jobCategoryEntity = interviewModifyDto.getJobCategoryEntity();
	}
}
