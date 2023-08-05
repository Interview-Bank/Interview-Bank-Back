package org.hoongoin.interviewbank.tempororay.infrastructure.repository;

import java.util.List;

import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import io.lettuce.core.dynamic.annotation.Param;

public interface TemporaryQuestionRepository extends JpaRepository<TemporaryQuestionEntity, Long> {

	List<TemporaryQuestionEntity> findByTemporaryInterviewEntityId(long temporaryInterviewId);

	@Modifying
	@Query("DELETE FROM TemporaryQuestionEntity tq WHERE tq.temporaryInterviewEntity.id = :temporaryInterviewId")
	void deleteTemporaryQuestionEntitiesByTemporaryInterviewId(
		@Param("temporaryInterviewId") long temporaryInterviewId);
}
