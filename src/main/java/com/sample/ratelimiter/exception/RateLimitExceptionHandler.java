package com.sample.ratelimiter.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RateLimitExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(RateLimitException.class)
	protected ResponseEntity<Object> handleRateLimitException(RateLimitException ex) {
	       return new ResponseEntity<>(ex.getMessage(), RateLimitException.HTTP_STATUS);
	}
}
