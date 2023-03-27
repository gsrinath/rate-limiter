package com.sample.ratelimiter.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisBean {

	@Value("${redis.host}")
	private String host;

	@Value("${redis.port}")
	private int port;
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}

	
}
