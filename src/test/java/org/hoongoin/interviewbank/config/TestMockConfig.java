package org.hoongoin.interviewbank.config;

import static org.mockito.Mockito.*;

import org.hoongoin.interviewbank.common.discord.DiscordCreator;
import org.hoongoin.interviewbank.exception.handler.IbControllerAdvice;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestMockConfig {

	@Bean
	public DiscordCreator discordCreator() {
		return mock(DiscordCreator.class);
	}

	@Bean
	public IbControllerAdvice ibControllerAdvice() {
		return mock(IbControllerAdvice.class);
	}

	@Bean
	public JavaMailSender javaMailSender() {
		return mock(JavaMailSender.class);
	}
}
