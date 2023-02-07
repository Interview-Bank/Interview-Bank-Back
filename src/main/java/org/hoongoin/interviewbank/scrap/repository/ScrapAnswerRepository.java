package org.hoongoin.interviewbank.scrap.repository;

import org.hoongoin.interviewbank.scrap.entity.ScrapAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScrapAnswerRepository extends JpaRepository<ScrapAnswerEntity, Long> {

	@Modifying
	@Query("DELETE from ScrapAnswerEntity scrap_answer WHERE scrap_answer.scrapQuestionEntity.id IN "
		+ "(SELECT scrap_question.id FROM ScrapQuestionEntity scrap_question WHERE scrap_question.scrapEntity.id = :scrap_id)")
	void deleteAllByScrapId(@Param("scrap_id") long scrapId);
}
