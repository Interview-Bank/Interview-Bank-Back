package org.hoongoin.interviewbank.scrap.infrastructure.entity;

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

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scrap_answer")
public class ScrapAnswerEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "scrap_question_id")
	private ScrapQuestionEntity scrapQuestionEntity;


	@Column(nullable = true, length = 1000)
	private String content;

	public void modifyEntity(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ScrapAnswerEntity{" +
			"id=" + id +
			", scrapQuestionEntity=" + scrapQuestionEntity +
			", content='" + content + '\'' +
			'}';
	}
}