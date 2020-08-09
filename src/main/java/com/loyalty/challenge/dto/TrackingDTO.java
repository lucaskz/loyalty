package com.loyalty.challenge.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Represents the formatted response from a Trace requested.
 *
 * @author Lucas Kaseta
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TrackingDTO {
	
	private String ip;
	
	private String date;
	
	private String country;
	
	private String isoCode;
	
	private List<String> languages;
	
	private List<String> currencies;
	
	private List<String> times;
	
	private String estimatedDistance;
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getIsoCode() {
		return isoCode;
	}
	
	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}
	
	public List<String> getLanguages() {
		return languages;
	}
	
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	public List<String> getCurrencies() {
		return currencies;
	}
	
	public void setCurrencies(List<String> currencies) {
		this.currencies = currencies;
	}
	
	public List<String> getTimes() {
		return times;
	}
	
	public void setTimes(List<String> times) {
		this.times = times;
	}
	
	public String getEstimatedDistance() {
		return estimatedDistance;
	}
	
	public void setEstimatedDistance(String estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}
}
