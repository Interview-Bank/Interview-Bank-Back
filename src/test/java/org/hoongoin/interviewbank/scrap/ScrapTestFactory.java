package org.hoongoin.interviewbank.scrap;

import java.time.LocalDateTime;

import org.hoongoin.interviewbank.account.infrastructure.entity.AccountEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.JobCategoryEntity;
import org.hoongoin.interviewbank.scrap.application.entity.Scrap;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapEntity;
import org.hoongoin.interviewbank.scrap.infrastructure.entity.ScrapQuestionEntity;

public class ScrapTestFactory {

	public static Scrap createScrap() {
		return Scrap.builder().scrapId(1).accountId(1).interviewId(1).title("title")
			.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
	}

	public static ScrapEntity createScrapEntity(InterviewEntity interviewEntity, AccountEntity accountEntity, JobCategoryEntity jobCategoryEntity) {
		return ScrapEntity.builder()
			.interviewEntity(interviewEntity)
			.accountEntity(accountEntity)
			.jobCategoryEntity(jobCategoryEntity)
			.title(interviewEntity.getTitle())
			.build();
	}

	public static ScrapQuestionEntity createScrapQuestionEntity(ScrapEntity scrapEntity, String content) {
		return ScrapQuestionEntity.builder()
			.scrapEntity(scrapEntity)
			.content(content)
			.build();
	}
}
