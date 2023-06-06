package org.hoongoin.interviewbank.interview.infrastructure.repository;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.TemporaryInterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TemporaryInterviewRepository extends JpaRepository<TemporaryInterviewEntity, Long> {
}
