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
import javax.persistence.UniqueConstraint;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;

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
@Table(name = "scrap_like",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_account_scrap",
			columnNames = {"account_id", "scrap_id"})
	}
)
public class ScrapLikeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "scrap_id")
	private ScrapEntity scrapEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "account_id")
	private AccountEntity accountEntity;

	@Column(nullable = false)
	private boolean like;

	public void modifyLike(boolean like){
		this.like = like;
	}

	@Override
	public String toString() {
		return "InterviewLikeEntity{" +
			"id=" + id +
			", scrapEntity=" + scrapEntity +
			", accountEntity=" + accountEntity +
			", like=" + like +
			'}';
	}
}
