package org.hoongoin.interviewbank.scrap.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.common.entity.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "scrap")
public class ScrapEntity extends BaseEntity {

	public ScrapEntity(long id, AccountEntity accountEntity, long interviewId, String title) {
		this.id = id;
		this.accountEntity = accountEntity;
		this.interviewId = interviewId;
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "account_id")
	private AccountEntity accountEntity;

	@Column(nullable = false)
	private long interviewId;

	@Column(nullable = false, length = 50)
	private String title;

	@Override
	public String toString() {
		return "ScrapEntity{" +
			"id=" + id +
			", accountEntity=" + accountEntity +
			", interviewId=" + interviewId +
			", title='" + title + '\'' +
			'}';
	}
}