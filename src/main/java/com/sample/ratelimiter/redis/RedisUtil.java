package com.sample.ratelimiter.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisUtil {

	@Autowired
	private RedisBean redisBean;
	
	private JedisConnectionFactory factory;

	public RedisConnection getRedisConnection() {
		if(factory==null) {
			initJedisConnectionFactory();
		}
		return factory.getConnection();
	}
	
	
    private void initJedisConnectionFactory() {
        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().build();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisBean.getHost(), redisBean.getPort());
        factory = new JedisConnectionFactory(configuration, jedisClientConfiguration);
        factory.afterPropertiesSet();
    }	
    
}
