package org.hoongoin.interviewbank.config;

import static org.mockito.Mockito.*;

import org.hoongoin.interviewbank.common.discord.DiscordCreator;
import org.hoongoin.interviewbank.common.gpt.GptRequestHandler;
import org.hoongoin.interviewbank.exception.handler.IbControllerAdvice;
import org.springframework.boot.test.context.TestConfiguration;

import com.amazonaws.services.s3.AmazonS3;

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

	@Bean
	public GptRequestHandler gptRequestHandler() {
		return mock(GptRequestHandler.class);
	}

	@Bean
	public AmazonS3 amazonS3() {
		return mock(AmazonS3.class);
	}

	@Bean
	public AmazonS3Config amazonS3Config() {
		return mock(AmazonS3Config.class);
	}
}
