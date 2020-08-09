package com.loyalty.challenge.service;

import java.math.BigDecimal;
import java.util.Map;

import com.loyalty.challenge.model.Currency;

public interface CurrencyService extends FallbackService<Currency, Currency> {
	
	Map<String, BigDecimal> getExchangeRate(String currencyCodes);
	
	Currency save(Currency currency);
}
