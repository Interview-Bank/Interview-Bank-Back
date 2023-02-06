package org.hoongoin.interviewbank.scrap.repository;

import java.util.List;

import org.hoongoin.interviewbank.interview.entity.QuestionEntity;
import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapQuestionRepository extends JpaRepository<ScrapQuestionEntity, Long> {

	@Query("SELECT scrap_question FROM ScrapQuestionEntity scrap_question "
		+ "WHERE scrap_question.scrapEntity.id = :scrap_id")
	List<ScrapQuestionEntity> findAllByScrapId(@Param("scrap_id") long scrapId);

	@Query("SELECT distinct scrap_question, scrap_answer FROM ScrapQuestionEntity scrap_question "
		+ "LEFT OUTER JOIN FETCH scrap_question.scrapAnswerEntities scrap_answer "
		+ "WHERE scrap_question.scrapEntity.id = :scrap_id")
	List<ScrapQuestionEntity> findAllWithScrapAnswerEntitiesByScrapId(@Param("scrap_id") long scrapId);

	@Modifying
	@Query("DELETE from ScrapQuestionEntity scrap_question "
		+ "WHERE scrap_question.scrapEntity.id = :scrap_id")
	void deleteAllByScrapId(@Param("scrap_id") long scrapId);
}
