package org.hoongoin.interviewbank.interview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "question")
public class QuestionEntity extends SoftDeletedBaseEntity {

	public QuestionEntity(long id, String content, InterviewEntity interviewEntity) {
		this.id = id;
		this.content = content;
		this.interviewEntity = interviewEntity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 100000)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "interview_id")
	private InterviewEntity interviewEntity;

	public void modifyContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "QuestionEntity{" +
			"id=" + id +
			", content='" + content + '\'' +
			", interviewEntity=" + interviewEntity +
			'}';
	}
}