package com.sample.ratelimiter.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.sample.ratelimiter.annotation.RateLimit;
import com.sample.ratelimiter.annotation.RateLimitOn;
import com.sample.ratelimiter.exception.RateLimitException;
import com.sample.ratelimiter.redis.RedisUtil;

@Aspect
@Component
@PropertySource("classpath:application.properties")
public class RateLimitProcessor implements EnvironmentAware {
	
	@Autowired
	private RedisUtil redisUtil;
	
	//@Autowired
	private Environment env;
	
	private static final String RATE_LIMIT_PREFIX = "ratelimit";

	private static final String MAX_ALLOWED_DEFAULT = "100";
	private static final String MAX_ALLOWED_PREFIX = "maxAllowed";
	
	private static final String WINDOW_SECS_DEFAULT = "100";
	private static final String WINDOW_SECS_PREFIX = "windowSecs";
	
	
	@Around("@annotation(com.sample.ratelimiter.annotation.RateLimit)")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		
		Method method = signature.getMethod();
		
		RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
		
		String rateLimitApiGroup = rateLimitAnnotation.rateLimitGroupName();
		RedisKeyForRateLimit key = new RedisKeyForRateLimit(rateLimitApiGroup);
		int index = 0;
		Annotation[][] parametersAnnotations = method.getParameterAnnotations();
		for(Annotation[] annotations : parametersAnnotations) {
			for(Annotation annotation:annotations) {
				if(annotation instanceof RateLimitOn) {
					RateLimitOn rateLimitOn = (RateLimitOn)annotation;
					key.addClientIdentifier(rateLimitOn.identifierName(), joinPoint.getArgs()[index]);
					break;
				}
			}
			index ++;
		}
		
		
		long methodCallTime = System.currentTimeMillis();
		byte[] keyBytes = SerializationUtils.serialize(key);
		
		RedisConnection redisConnection = redisUtil.getRedisConnection();
		long startTimeToCheck = methodCallTime - getWindowSecs(rateLimitApiGroup)*1000;
		long endTimeToCheck = methodCallTime;
		
		long currentCount = redisConnection.zSetCommands().zCount(keyBytes, startTimeToCheck, endTimeToCheck);
		if(currentCount>=getMaxAllowed(rateLimitApiGroup)) {
			throw new RateLimitException("Too many requests");
		}
		redisConnection.zAdd(keyBytes, endTimeToCheck,SerializationUtils.serialize(methodCallTime));
		redisConnection.zSetCommands().zRemRangeByScore(keyBytes, 0, startTimeToCheck);
		
		return joinPoint.proceed();
		
	}
	
	private long getMaxAllowed(String rateLimitGroupName) {
		String envKey = RATE_LIMIT_PREFIX + "." + rateLimitGroupName + "." + MAX_ALLOWED_PREFIX;
		return Long.valueOf(env.getProperty(envKey, MAX_ALLOWED_DEFAULT));
	}
	
	private long getWindowSecs(String rateLimitGroupName) {
		String envKey = RATE_LIMIT_PREFIX + "." + rateLimitGroupName + "." + WINDOW_SECS_PREFIX;
		return Long.valueOf(env.getProperty(envKey, WINDOW_SECS_DEFAULT));
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.env = environment;
	}
	

}
