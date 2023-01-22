package org.hoongoin.interviewbank.interview.repository;

import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long> {
}