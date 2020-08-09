package com.loyalty.challenge.service;

import com.loyalty.challenge.model.Country;

public interface CountryRepositoryService extends FallbackService<String, Country> {
	
	Country findTopByCountryCode(String countryCode);
	
}
