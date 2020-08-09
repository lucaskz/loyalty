package com.loyalty.challenge.service;

import com.loyalty.challenge.model.Country;

public interface CountryService extends FallbackService<Country, Country> {
	
	Country findCountryByCode(String countryCode);
	
	Country findCountryFromClient(String countryCode);
	
	Country save(Country country);
}

