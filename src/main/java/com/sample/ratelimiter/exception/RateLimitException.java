package com.sample.ratelimiter.exception;

import org.springframework.http.HttpStatus;

public class RateLimitException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public static final HttpStatus HTTP_STATUS = HttpStatus.TOO_MANY_REQUESTS;

	public RateLimitException(String msg) {
		super(msg);
	}

}
