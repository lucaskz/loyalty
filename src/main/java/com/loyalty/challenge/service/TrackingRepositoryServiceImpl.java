package com.loyalty.challenge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.repository.TrackingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class TrackingRepositoryServiceImpl extends AbstractFallbackService<String, Tracking> implements TrackingRepositoryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TrackingRepositoryServiceImpl.class);
	
	private TrackingRepository trackingRepository;
	
	public TrackingRepositoryServiceImpl(TrackingRepository trackingRepository) {
		this.trackingRepository = trackingRepository;
	}
	
	@Override
	@CircuitBreaker(name = "storageServiceCircuitBreaker", fallbackMethod = "fallback")
	public Tracking findTopByIp(String ip) {
		return this.trackingRepository.findTopByIp(ip);
	}
	
	@Override
	public Tracking save(Tracking tracking) {
		return this.trackingRepository.save(tracking);
	}
	
	@Override
	public Tracking fallback(String ip, Throwable throwable) {
		return super.fallback(ip, throwable);
	}
	
	@Override
	protected String getServiceName() {
		return "Tracking Repository Service";
	}
	
	@Override
	protected Tracking fallbackResponse(String entity) {
		// returns empty response ( null )
		return null;
	}
	
	@Override
	protected Logger getLogger() {
		return LOGGER;
	}
}
