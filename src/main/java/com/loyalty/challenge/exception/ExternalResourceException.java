package com.loyalty.challenge.exception;

public class ExternalResourceException extends RuntimeException {
	
	public ExternalResourceException(String message, Throwable cause){
		super(message, cause);
	}
}
