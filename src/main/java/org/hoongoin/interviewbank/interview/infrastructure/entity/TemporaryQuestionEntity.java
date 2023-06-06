package org.hoongoin.interviewbank.interview.infrastructure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hoongoin.interviewbank.common.entity.BaseEntity;

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
@Table(name = "temporary_question")
public class TemporaryQuestionEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 65535)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "temporary_interview_id")
	private TemporaryInterviewEntity temporaryInterviewEntity;

	public void modifyContent(String content) {
		this.content = content;
	}
}
