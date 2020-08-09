package com.loyalty.challenge.service;

import java.math.BigDecimal;
import java.util.Map;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.client.CurrencyClient;
import com.loyalty.challenge.model.Currency;
import com.loyalty.challenge.repository.CurrencyRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Stores {@link Currency} and implements a {@link CircuitBreaker} in case of storage failure.
 * Also, implements {@link Cacheable} strategy to avoid unnecessary waste of internal and external resources.
 *
 * @author Lucas Kaseta
 */

@Service
public class CurrencyServiceImpl extends AbstractFallbackService<Currency, Currency> implements CurrencyService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyServiceImpl.class);
	
	private CurrencyClient currencyClient;
	private CurrencyRepository currencyRepository;
	
	public CurrencyServiceImpl(CurrencyClient currencyClient, CurrencyRepository currencyRepository) {
		this.currencyClient = currencyClient;
		this.currencyRepository = currencyRepository;
	}
	
	@Cacheable(value = "currencies", cacheManager = "currencyCacheManager")
	@Override
	public Map<String, BigDecimal> getExchangeRate(String currencyCodes) {
		return this.currencyClient.call(currencyCodes);
	}
	
	@Transactional
	@CircuitBreaker(name = "storageServiceCircuitBreaker", fallbackMethod = "fallback")
	@Override
	public Currency save(Currency currency) {
		return this.currencyRepository.save(currency);
	}
	
	@Override
	public Currency fallback(Currency currency, Throwable throwable) {
		return super.fallback(currency, throwable);
	}
	
	@Override
	protected String getServiceName() {
		return "Currency Service";
	}
	
	@Override
	protected Currency fallbackResponse(Currency entity) {
		return entity;
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
