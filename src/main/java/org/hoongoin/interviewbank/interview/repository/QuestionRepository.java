package org.hoongoin.interviewbank.interview.repository;

import java.util.List;

import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

	@Query("SELECT q FROM QuestionEntity q JOIN FETCH q.interviewEntity WHERE q.interviewEntity = :interviewEntity")
	List<QuestionEntity> findQuestionEntitiesByInterviewEntity(@Param("interviewEntity") InterviewEntity interviewEntity);
}