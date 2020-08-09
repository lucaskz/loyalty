package com.loyalty.challenge.exception;

public class CountryNotFoundException extends RuntimeException {
	
	public CountryNotFoundException(String message){
		super(message);
	}
}
