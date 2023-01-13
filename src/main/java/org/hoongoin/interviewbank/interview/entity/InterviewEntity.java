package org.hoongoin.interviewbank.interview.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewEntity extends SoftDeletedBaseEntity {

	public InterviewEntity(long id, String title, AccountEntity accountEntity) {
		this.id = id;
		this.title = title;
		this.accountEntity = accountEntity;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 50)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "account_id")
	private AccountEntity accountEntity;

	@Override
	public String toString() {
		return "InterviewEntity{" +
			"id=" + id +
			", title='" + title + '\'' +
			", accountEntity=" + accountEntity +
			'}';
	}
}
