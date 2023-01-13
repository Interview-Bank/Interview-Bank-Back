package org.hoongoin.interviewbank.scrap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hoongoin.interviewbank.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScrapAnswerEntity extends BaseEntity {

	public ScrapAnswerEntity(long id, ScrapQuestionEntity scrapQuestionEntity, String content) {
		this.id = id;
		this.scrapQuestionEntity = scrapQuestionEntity;
		this.content = content;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "scrap_interview_id")
	private ScrapQuestionEntity scrapQuestionEntity;


	@Column(nullable = false, length = 1000)
	private String content;

	@Override
	public String toString() {
		return "ScrapAnswerEntity{" +
			"id=" + id +
			", scrapQuestionEntity=" + scrapQuestionEntity +
			", content='" + content + '\'' +
			'}';
	}
}
