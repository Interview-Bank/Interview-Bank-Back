package org.hoongoin.interviewbank.interview.repository;

import java.util.List;

import org.hoongoin.interviewbank.interview.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

	@Query("SELECT question FROM QuestionEntity question WHERE question.interviewEntity.id = :interview_id")
	List<QuestionEntity> findAllByInterviewId(@Param("interview_id") Long interviewId);

	List<QuestionEntity> findQuestionEntitiesByInterviewEntity(@Param("interviewEntity") InterviewEntity interviewEntity);
}
