package com.loyalty.challenge.exception;

import java.util.Date;

/**
 * Error response representation wich includes a timestamp, a message and details of what caused such exception.
 *
 * @author Lucas Kaseta
 */

public class ErrorDetail {
	
	private Date timestamp;
	private String message;
	private String details;
	
	ErrorDetail(Date timestamp, String message, String details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDetails() {
		return details;
	}
}
