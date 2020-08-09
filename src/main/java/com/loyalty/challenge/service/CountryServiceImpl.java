package com.loyalty.challenge.service;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.client.CountryClient;
import com.loyalty.challenge.helper.DistanceHelper;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.Currency;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * This service can store and load a {@link Country}. Also takes the model relationships into account.
 * In case of an eventual storage failure, implements a {@link CircuitBreaker} strategy, allowing to fulfil the initial request
 * and skip future storage calls until a period of time is waited.
 * Implements a {@link Cacheable} strategy to avoid unnecessary waste of resources.
 *
 * @author Lucas Kaseta
 */

@Service
public class CountryServiceImpl extends AbstractFallbackService<Country, Country> implements CountryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryServiceImpl.class);
	
	private CountryClient countryClient;
	
	private CountryRepositoryServiceImpl countryRepositoryService;
	
	private CurrencyService currencyService;
	
	public CountryServiceImpl(CountryClient countryClient, CountryRepositoryServiceImpl countryRepositoryService, CurrencyService currencyService) {
		this.countryClient = countryClient;
		this.countryRepositoryService = countryRepositoryService;
		this.currencyService = currencyService;
	}
	
	@Transactional
	@CircuitBreaker(name = "storageServiceCircuitBreaker", fallbackMethod = "fallback")
	@Override
	public Country save(Country country) {
		country.getCurrencies().forEach(currency -> {
			if (currency.getId() == null) {
				Currency storedCurrency = this.currencyService.save(currency);
				currency.setId(storedCurrency.getId());
			}
		});
		if (country.getCurrencies().stream().allMatch(currency -> currency.getId() != null)) {
			country = this.countryRepositoryService.save(country);
		}
		return country;
	}
	
	@Override
	public Country findCountryByCode(String countryCode) {
		Country country = this.countryRepositoryService.findTopByCountryCode(countryCode);
		
		if (country == null) {
			country = findCountryFromClient(countryCode);
		}
		
		return country;
	}
	
	@Cacheable(value = "countries", cacheManager = "countryCacheManager")
	@Override
	public Country findCountryFromClient(String countryCode) {
		Country country = this.countryClient.call(countryCode);
		country.setCountryCode(countryCode);
		country.setDistance(DistanceHelper.measureDistance(country.getLat(), country.getLng()));
		return country;
	}
	
	@Override
	public Country fallback(Country country, Throwable t) {
		return super.fallback(country, t);
	}
	
	@Override
	protected String getServiceName() {
		return "Country Service";
	}
	
	@Override
	protected Country fallbackResponse(Country entity) {
		return entity;
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
