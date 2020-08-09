package com.loyalty.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.repository.CountryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CountryRepositoryServiceImpl extends AbstractFallbackService<String, Country> implements CountryRepositoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryRepositoryServiceImpl.class);
	
	private CountryRepository countryRepository;
	
	public CountryRepositoryServiceImpl(CountryRepository countryRepository) {
		this.countryRepository = countryRepository;
	}
	
	public Country save(Country country) {
		return this.countryRepository.save(country);
	}
	
	@CircuitBreaker(name = "storageServiceCircuitBreaker", fallbackMethod = "fallback")
	public Country findTopByCountryCode(String countryCode) {
		return this.countryRepository.findTopByCountryCode(countryCode);
	}
	
	@Override
	public Country fallback(String countryCode, Throwable throwable) {
		return super.fallback(countryCode, throwable);
	}
	
	@Override
	protected String getServiceName() {
		return "Country Repository Service";
	}
	
	@Override
	protected Country fallbackResponse(String entity) {
		// returns no result found ( null )
		return null;
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
