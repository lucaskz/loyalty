package com.loyalty.challenge.dto;

/**
 *  Holds the error response from {@link com.loyalty.challenge.client.CurrencyClient}
 *
 * @author Lucas Kaseta
 */

public class ErrorDTO {
	
	private String code;
	private String type;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
