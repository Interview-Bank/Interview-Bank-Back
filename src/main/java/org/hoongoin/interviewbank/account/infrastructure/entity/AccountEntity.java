package org.hoongoin.interviewbank.account.infrastructure.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.*;

import org.hoongoin.interviewbank.account.application.entity.AccountType;
import org.hoongoin.interviewbank.common.entity.SoftDeletedBaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "account", uniqueConstraints = {
	@UniqueConstraint(name = "unique_account", columnNames = {"accountType", "email"})})
public class AccountEntity extends SoftDeletedBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, length = 20)
	private String nickname;

	@Column(nullable = false, length = 120)
	private String email;

	@Column(length = 61)
	private String password;

	@Enumerated(EnumType.STRING)
	private AccountType accountType;

	@Column(length = 300)
	private String imageUrl;

	@Column
	private LocalDateTime passwordUpdatedAt;

	public void resetPassword(String password) {
		this.passwordUpdatedAt = LocalDateTime.now();
		this.password = password;
	}

	public void editNickname(String nickname) {
		this.nickname = nickname;
	}

	public void uploadImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "AccountEntity{" +
			"id=" + id +
			", nickname='" + nickname + '\'' +
			", email='" + email + '\'' +
			", password='" + password + '\'' +
			'}';
	}
}
