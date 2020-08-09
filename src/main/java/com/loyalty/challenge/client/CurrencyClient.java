package com.loyalty.challenge.client;

import static com.loyalty.challenge.constant.CurrencyConstants.BASE_CURRENCY_EXCHANGE;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.dto.ErrorDTO;
import com.loyalty.challenge.dto.ExchangeRateDTO;
import com.loyalty.challenge.exception.CurrencyClientException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

/**
 * Produces a GET request in order to obtain a set of currencies exchange rates.
 * Similar to {@link CountryClient} implements a {@link RateLimiter} strategy to protect the external service
 * from traffic overload.
 * Throws {@link CurrencyClientException} when the api returns the property success in false.
 *
 * @author Lucas Kaseta
 */

@Component
public class CurrencyClient extends AbstractGetClient<ExchangeRateDTO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyClient.class);
	
	@Value("${currency.api.key}")
	private String apiKey;
	
	public CurrencyClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RateLimiter(name = "currencyClientRateLimiter")
	public Map<String, BigDecimal> call(String currencyCodes) {
		ExchangeRateDTO exchangeRateDTO = call(this.apiKey, BASE_CURRENCY_EXCHANGE, currencyCodes);
		return exchangeRateDTO.getRates();
	}
	
	@Override
	protected String getClientName() {
		return "Rest Currency Client";
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	protected Class<ExchangeRateDTO> getResponseType() {
		return ExchangeRateDTO.class;
	}
	
	@Override
	protected String getUrl() {
		return ClientConstants.DATA_FIXER_URI;
	}
	
	@Override
	protected void validateResponse(ExchangeRateDTO response) {
		if (!response.isSuccess()) {
			String message = String.format("Currency client returned failure state : %s", Optional.ofNullable(response.getError()).map(ErrorDTO::getType).orElse("Unknow"));
			CurrencyClientException e = new CurrencyClientException(message);
			this.getLogger().warn(message, e);
			throw e;
		}
	}
}
