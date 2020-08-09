package com.loyalty.challenge.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.exception.CountryNotFoundException;
import com.loyalty.challenge.model.Country;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

/**
 * This client produces a GET request in order to retrieve a {@link Country} information.
 * A {@link com.loyalty.challenge.deserializer.CountryDeserializer} is called in order to deserialize the response.
 * {@link RateLimiter} strategy protects the external api from traffic overload.
 *
 * @author Lucas Kaseta
 */

@Component
public class CountryClient extends AbstractGetClient<Country> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CountryClient.class);
	
	public CountryClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Override
	protected String getClientName() {
		return "Rest Country Client";
	}
	
	@RateLimiter(name = "countryClientRateLimiter")
	public Country call(String countryCode) {
		return super.call(countryCode);
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	protected Class<Country> getResponseType() {
		return Country.class;
	}
	
	@Override
	protected String getUrl() {
		return ClientConstants.REST_COUNTRIES_URI;
	}
	
	@Override
	protected void validateResponse(Country response) {
		if (response == null) {
			throw new CountryNotFoundException("Could not find country");
		}
	}
}
