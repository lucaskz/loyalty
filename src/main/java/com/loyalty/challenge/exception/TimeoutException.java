package com.loyalty.challenge.exception;

public class TimeoutException extends RuntimeException {
	
	public TimeoutException(String message, Throwable cause) {
		super(message, cause);
	}
}
