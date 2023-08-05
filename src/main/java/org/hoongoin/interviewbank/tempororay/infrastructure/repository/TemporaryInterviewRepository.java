package org.hoongoin.interviewbank.tempororay.infrastructure.repository;

import org.hoongoin.interviewbank.tempororay.infrastructure.entity.TemporaryInterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryInterviewRepository extends JpaRepository<TemporaryInterviewEntity, Long> {

	void deleteTemporaryInterviewEntityById(long id);
}
