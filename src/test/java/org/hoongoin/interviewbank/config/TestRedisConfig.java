package org.hoongoin.interviewbank.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import redis.embedded.RedisServer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestRedisConfig {
	private RedisServer redisServer;
	@Value("${spring.redis.port}")
	private int port;

	@PostConstruct
	public void startRedis() {
		try {
			redisServer = new RedisServer(port);
			redisServer.start();
		} catch (Exception e) {
		}
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}
}
