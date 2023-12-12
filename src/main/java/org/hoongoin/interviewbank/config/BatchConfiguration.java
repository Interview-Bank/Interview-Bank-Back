package org.hoongoin.interviewbank.config;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import org.hoongoin.interviewbank.interview.domain.QuestionCommandServiceAsync;
import org.hoongoin.interviewbank.interview.infrastructure.entity.QuestionEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	private final QuestionCommandServiceAsync questionCommandServiceAsync;

	@Bean
	public ItemReader<QuestionEntity> reader() {
		return new JpaCursorItemReaderBuilder<QuestionEntity>()
			.name("questionEntityReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("select q from QuestionEntity q where q.gptAnswer is null or q.gptAnswer = '' "
				+ "or q.gptAnswer = 'GPT가 답변을 생성중입니다.'")
			.build();
	}

	@Bean
	public ItemProcessor<QuestionEntity, QuestionEntity> processor() {
		return questionEntity -> questionEntity;
	}

	@Bean
	public ItemWriter<QuestionEntity> writer() {
		return questionEntities -> {
			questionCommandServiceAsync.updateAllGptAnswer((List<QuestionEntity>) questionEntities);
		};
	}

	@Bean
	public Step updateGptAnswerStep() {
		return stepBuilderFactory.get("updateGptAnswerStep")
			.<QuestionEntity, QuestionEntity>chunk(10)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean
	public Job updateGptAnswerJob() {
		return jobBuilderFactory.get("updateGptAnswerJob")
			.start(updateGptAnswerStep())
			.build();
	}
}
