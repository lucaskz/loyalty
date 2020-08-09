package com.loyalty.challenge.dto;

/**
 * Represents the response from {@link com.loyalty.challenge.client.IpClient} external API.
 *
 * @author Lucas Kaseta
 */

public class CountryDTO {
	
	private String countryCode;
	
	private String name;
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
