package org.hoongoin.interviewbank.interview.infrastructure.repository;

import java.util.List;

import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {

	@Query("SELECT question FROM QuestionEntity question WHERE question.interviewEntity.id = :interview_id")
	List<QuestionEntity> findAllByInterviewId(@Param("interview_id") Long interviewId);

	@Query("SELECT question FROM QuestionEntity question WHERE question.deletedFlag = FALSE ")
	List<QuestionEntity> findQuestionEntitiesByInterviewEntity(@Param("interviewEntity") InterviewEntity interviewEntity);
}
