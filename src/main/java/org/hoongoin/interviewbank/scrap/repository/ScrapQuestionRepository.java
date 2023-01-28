package org.hoongoin.interviewbank.scrap.repository;

import org.hoongoin.interviewbank.scrap.entity.ScrapQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapQuestionRepository extends JpaRepository<ScrapQuestionEntity, Long> {
}
