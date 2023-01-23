package org.hoongoin.interviewbank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;

@EnableSpringHttpSession
@Configuration
public class RedisConfig {

	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.host}")
	private String host;

	@Bean
	public LettuceConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	public HeaderHttpSessionIdResolver httpSessionIdResolver() {
		return HeaderHttpSessionIdResolver.xAuthToken();
	}
}
