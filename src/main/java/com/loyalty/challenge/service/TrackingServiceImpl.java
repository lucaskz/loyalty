package com.loyalty.challenge.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.model.Currency;
import com.loyalty.challenge.model.Tracking;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

/**
 * Manages a {@link Tracking}. Can find a previous tracking using a IP. Also takes into account the relationships to persist
 * a whole {@link Tracking}.
 * In case of a storage failure, implements a {@link CircuitBreaker} strategy, allowing the avoid performance losses.
 * When the currency service starts to fail, the currency detail(exchange rate) is discarted to allow the application to keep working.
 *
 * @author Lucas Kaseta
 */

@Service
public class TrackingServiceImpl extends AbstractFallbackService<Tracking, Tracking> implements TrackingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackingServiceImpl.class);
	
	private TrackingRepositoryService trackingRepositoryService;
	
	private CurrencyService currencyService;
	
	private CountryService countryService;
	
	public TrackingServiceImpl(TrackingRepositoryService trackingRepositoryService, CurrencyService currencyService, CountryService countryService) {
		this.trackingRepositoryService = trackingRepositoryService;
		this.currencyService = currencyService;
		this.countryService = countryService;
	}
	
	@Transactional
	@Override
	@CircuitBreaker(name = "storageServiceCircuitBreaker", fallbackMethod = "fallback")
	public Tracking save(Tracking tracking) {
		if (tracking.getCountry().getId() == null) {
			this.countryService.save(tracking.getCountry());
		}
		if (tracking.getCountry().getId() != null) {
			tracking = this.trackingRepositoryService.save(tracking);
		}
		return tracking;
	}
	
	@Override
	@CircuitBreaker(name = "currencyExchangeCircuitBreaker", fallbackMethod = "fallback")
	public Tracking fillCurrencyExchange(Tracking tracking) {
		String currencyCodes = tracking.getCountry().getCurrencies().stream().map(Currency::getCode).collect(Collectors.joining(","));
		Map<String, BigDecimal> rates = this.currencyService.getExchangeRate(currencyCodes);
		tracking.getCountry().getCurrencies().forEach(currency -> currency.setQuote(rates.get(currency.getCode())));
		return tracking;
	}
	
	@Override
	public Tracking findTrackingByIp(String ip) {
		return this.trackingRepositoryService.findTopByIp(ip);
	}
	
	@Override
	public Tracking fallback(Tracking tracking, Throwable t) {
		return super.fallback(tracking, t);
	}
	
	@Override
	protected String getServiceName() {
		return "Tracking Service";
	}
	
	@Override
	protected Tracking fallbackResponse(Tracking entity) {
		return entity;
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
