package com.sample.ratelimiter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.sample.ratelimiter.annotation.RateLimit;
import com.sample.ratelimiter.annotation.RateLimitOn;

@RestController
public class HelloWorldController {
	
	

	@GetMapping("/hello1")
	@RateLimit(rateLimitGroupName = "apiGroup")
	public String helloWorld1(@RateLimitOn(identifierName = "uuid") @RequestHeader(name = "uuid") String clientUUID) {
		return "HelloWorld 1";
	}
	
	@GetMapping("/hello2")
	@RateLimit(rateLimitGroupName = "uiGroup")
	public String helloWorld2(@RateLimitOn(identifierName = "firstName") @RequestHeader(name = "firstName") String firstName,
			@RateLimitOn(identifierName = "lastName") @RequestHeader(name = "lastName") String lastName) {
		return "HelloWorld 2";
	}
	
	@GetMapping("/bye1")
	@RateLimit(rateLimitGroupName = "apiGroup")
	public String bye1(@RateLimitOn(identifierName = "uuid") @RequestHeader(name = "uuid") String clientUUID) {
		return "HelloWorld 1";
	}
	
	@GetMapping("/bye2")
	@RateLimit(rateLimitGroupName = "uiGroup")
	public String bye2(@RateLimitOn(identifierName = "firstName") @RequestHeader(name = "firstName") String firstName,
			@RateLimitOn(identifierName = "lastName") @RequestHeader(name = "lastName") String lastName) {
		return "HelloWorld 2";
	}
	
}
