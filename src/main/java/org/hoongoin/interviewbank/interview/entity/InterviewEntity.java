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

import org.hoongoin.interviewbank.account.entity.AccountEntity;
import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

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
@Table(name = "interview")
public class InterviewEntity extends SoftDeletedBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 50)
	private String title;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "account_id")
	private AccountEntity accountEntity;

	public void modifyEntity(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "InterviewEntity{" +
			"id=" + id +
			", title='" + title + '\'' +
			", accountEntity=" + accountEntity +
			'}';
	}
}