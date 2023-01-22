package org.hoongoin.interviewbank.interview.repository;

import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
}
