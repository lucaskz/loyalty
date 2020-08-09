package com.loyalty.challenge.client;

import org.slf4j.Logger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import com.loyalty.challenge.exception.ExternalResourceException;
import com.loyalty.challenge.exception.TimeoutException;

/**
 * Abstraction of a client that makes an external GET call. Allows each sub-class to define custom
 * error handling method
 *
 * @author Lucas Kaseta
 */

public abstract class AbstractGetClient<T> {
	
	private static final String ERROR_MESSAGE = "Error on %s. Exception: %s";
	private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource not found on %s";
	
	RestTemplate restTemplate;
	
	public T call(Object... parameters) {
		T response = null;
		try {
			response = this.restTemplate.getForObject(this.getUrl(), this.getResponseType(), parameters);
		} catch (ResourceAccessException e) {
			this.handleTimeOutException(e);
		} catch (Exception e) {
			this.handleException(e);
		}
		this.validateResponse(response);
		return response;
	}
	
	private void handleTimeOutException(ResourceAccessException e) {
		String message = String.format(ERROR_MESSAGE, this.getClientName(), e);
		this.getLogger().warn(message, e);
		throw new TimeoutException(message, e);
	}
	
	private void handleException(Exception e) {
		String message = String.format(ERROR_MESSAGE, this.getClientName(), e);
		this.getLogger().error(message, e);
		throw new ExternalResourceException(message, e);
	}
	
	protected void handleResourceNotFoundException() {
		String message = String.format(RESOURCE_NOT_FOUND_MESSAGE, this.getClientName());
		ResourceNotFoundException e = new ResourceNotFoundException(message);
		this.getLogger().warn(message, e);
		throw e;
	}
	
	protected abstract String getClientName();
	
	protected abstract Logger getLogger();
	
	protected abstract Class<T> getResponseType();
	
	protected abstract String getUrl();
	
	protected void validateResponse(T response) {
	}
	
}
