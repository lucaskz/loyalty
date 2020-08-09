package com.loyalty.challenge.service;

import org.slf4j.Logger;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

/**
 * Abstract definition of a service with fallback method in case of something starts to fail.
 *
 * @author Lucas Kaseta
 */

public abstract class AbstractFallbackService<T, S> implements FallbackService<T, S> {
	
	public S fallback(T entity, Throwable throwable) {
		if (!CallNotPermittedException.class.isAssignableFrom(throwable.getClass())) {
			//Make propper metric alert
			this.getLogger().error(String.format("Called fallback method, reason: %s", throwable.getMessage()), throwable);
		} else {
			this.getLogger().error(String.format("Circuit breaker OPEN on: %s", this.getServiceName()));
		}
		return this.fallbackResponse(entity);
	}
	
	protected abstract String getServiceName();
	
	protected abstract S fallbackResponse(T entity);
	
	protected abstract Logger getLogger();
}
