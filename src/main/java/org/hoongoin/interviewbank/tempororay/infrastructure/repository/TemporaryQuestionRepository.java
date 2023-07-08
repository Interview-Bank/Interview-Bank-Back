package org.hoongoin.interviewbank.tempororay.infrastructure.repository;

import java.util.List;

import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryQuestionRepository extends JpaRepository<TemporaryQuestionEntity, Long> {

	List<TemporaryQuestionEntity> findByTemporaryInterviewEntityId(long temporaryInterviewId);
}
