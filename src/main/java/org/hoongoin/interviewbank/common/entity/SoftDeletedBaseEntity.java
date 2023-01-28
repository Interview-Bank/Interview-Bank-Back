package org.hoongoin.interviewbank.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@MappedSuperclass
@DynamicInsert
public class SoftDeletedBaseEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@ColumnDefault("0")
	@Column(nullable = false)
	private Boolean deletedFlag = false;

	@Column
	private LocalDateTime deletedAt;

	public void deleteEntityByFlag() {
		this.setDeletedFlag(true);
		this.setDeletedAt(LocalDateTime.now());
	}
}