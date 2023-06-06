package org.hoongoin.interviewbank.interview.infrastructure.repository;

import org.hoongoin.interviewbank.interview.infrastructure.entity.TemporaryQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryQuestionRepository extends JpaRepository<TemporaryQuestionEntity, Long> {

}
