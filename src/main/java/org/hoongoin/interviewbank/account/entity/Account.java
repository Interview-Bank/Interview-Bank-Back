package org.hoongoin.interviewbank.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends SoftDeletedBaseEntity {

	public Account(long id, String nickname, String email, String password) {
		this.id = id;
		this.nickname = nickname;
		this.email = email;
		this.password = password;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false, length = 120)
	private String email;

	@Column(nullable = false, length = 25)
	private String password;
}
