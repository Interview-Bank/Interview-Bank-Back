package org.hoongoin.interviewbank.interview.infrastructure.repository;

import static org.hoongoin.interviewbank.interview.infrastructure.entity.QInterviewEntity.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.hoongoin.interviewbank.interview.enums.CareerYear;
import org.hoongoin.interviewbank.interview.enums.InterviewPeriod;
import org.hoongoin.interviewbank.interview.infrastructure.entity.InterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QInterviewEntity;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QJobCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface InterviewRepository extends JpaRepository<InterviewEntity, Long>,
	QuerydslPredicateExecutor<InterviewEntity> {

	Page<InterviewEntity> findByDeletedFlagOrderByCreatedAtDesc(Boolean deletedFlag, Pageable pageable);

	default Page<InterviewEntity> findAllByTitleAndJobCategoryIdsAndStartDateAndEndDatePageableOrderByCreateTimeDesc(
		String query, List<Long> jobCategoryIds, LocalDateTime startDateTime,
		LocalDateTime endDateTime, InterviewPeriod interviewPeriod,
		CareerYear careerYear, Pageable pageable) {

		QInterviewEntity interviewEntity = QInterviewEntity.interviewEntity;
		QJobCategoryEntity jobCategoryEntity = QJobCategoryEntity.jobCategoryEntity;

		BooleanExpression predicate = interviewEntity.deletedFlag.eq(Boolean.FALSE);

		if (query != null) {
			predicate = predicate.and(interviewEntity.title.like("%" + query + "%"));
		}

		if (jobCategoryIds != null) {
			predicate = predicate.and(interviewEntity.jobCategoryEntity.id.in(jobCategoryIds)
				.or(interviewEntity.jobCategoryEntity.parentJobCategory.id.in(jobCategoryIds)));
		}

		if (startDateTime != null) {
			predicate = predicate.and(interviewEntity.createdAt.goe(startDateTime));
		}

		if (endDateTime != null) {
			predicate = predicate.and(interviewEntity.createdAt.loe(endDateTime));
		}

		if (interviewPeriod != null) {
			predicate = predicate.and(interviewEntity.interviewPeriod.eq(interviewPeriod));
		}

		if (careerYear != null) {
			predicate = predicate.and(interviewEntity.careerYear.eq(careerYear));
		}

		return findAll(predicate, pageable);
	}

	default Page<InterviewEntity> findByAccountEntityIdAndDeleteFlag(Pageable pageable, long accountId) {
		BooleanExpression hasAccountId = interviewEntity.accountEntity.id.eq(accountId);
		BooleanExpression hasDeleteFlag = interviewEntity.deletedFlag.eq(Boolean.FALSE);
		return findAll(hasAccountId.and(hasDeleteFlag), pageable);
	}
}
