package org.hoongoin.interviewbank.scrap.infrastructure.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "scrap_question")
public class ScrapQuestionEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "scrap_id")
	private ScrapEntity scrapEntity;

	@Column(nullable = false, length = 1000)
	private String content;

	@OneToMany(mappedBy = "scrapQuestionEntity")
	List<ScrapAnswerEntity> scrapAnswerEntities = new ArrayList<>();

	@Override
	public String toString() {
		return "ScrapQuestionEntity{" +
			"id=" + id +
			", scrapEntity=" + scrapEntity +
			", content='" + content + '\'' +
			'}';
	}
}