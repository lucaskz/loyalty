package com.loyalty.challenge.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Represents the currency exchange rate for each currency consulted
 *
 * @author Lucas Kaseta
 */

public class ExchangeRateDTO {
	
	private Long timestamp;
	private boolean success;
	private String base;
	private Map<String, BigDecimal> rates;
	private ErrorDTO error;
	
	public Long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getBase() {
		return base;
	}
	
	public void setBase(String base) {
		this.base = base;
	}
	
	public Map<String, BigDecimal> getRates() {
		return rates;
	}
	
	public void setRates(Map<String, BigDecimal> rates) {
		this.rates = rates;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
	public ErrorDTO getError() {
		return error;
	}
	
	public void setError(ErrorDTO error) {
		this.error = error;
	}
}
