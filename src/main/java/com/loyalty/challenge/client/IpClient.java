package com.loyalty.challenge.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.dto.CountryDTO;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

/**
 * Consumes an external service to figure out from which country the IP comes.
 * Has a {@link RateLimiter} strategy to protect the external api from traffic overload.
 * Throws {@link org.springframework.data.rest.webmvc.ResourceNotFoundException} on empty response.
 *
 * @author Lucas Kaseta
 */

@Component
public class IpClient extends AbstractGetClient<CountryDTO> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IpClient.class);
	
	public IpClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@RateLimiter(name = "ipClientRateLimiter")
	public CountryDTO call(String ip) {
		return super.call(ip);
	}
	
	@Override
	protected String getClientName() {
		return "Rest Ip Client";
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
	
	@Override
	protected Class<CountryDTO> getResponseType() {
		return CountryDTO.class;
	}
	
	@Override
	protected String getUrl() {
		return ClientConstants.IP_TO_COUNTRY_URI;
	}
	
	@Override
	protected void validateResponse(CountryDTO response) {
		if (response.getCountryCode() == null) {
			this.handleResourceNotFoundException();
		}
	}
}
