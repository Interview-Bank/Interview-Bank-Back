package org.hoongoin.interviewbank.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

	@Bean(name = "mail")
	public Executor mailTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(50);
		executor.setThreadNamePrefix("UserThread-");
		executor.initialize();
		return executor;
	}

	@Bean(name = "gpt")
	public Executor gptTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("UserThread-");
		executor.initialize();
		return executor;
	}

	@Override
	public IbAsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new IbAsyncUncaughtExceptionHandler();
	}
}
