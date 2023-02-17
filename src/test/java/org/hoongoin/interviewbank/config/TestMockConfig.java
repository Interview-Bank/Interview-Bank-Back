package org.hoongoin.interviewbank.config;

import static org.mockito.Mockito.*;

import org.hoongoin.interviewbank.common.discord.DiscordCreator;
import org.hoongoin.interviewbank.exception.handler.GlobalControllerAdvice;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestMockConfig {

	@Bean
	public DiscordCreator discordCreator() {
		return mock(DiscordCreator.class);
	}

	@Bean
	public GlobalControllerAdvice globalControllerAdvice() {
		return mock(GlobalControllerAdvice.class);
	}
}
