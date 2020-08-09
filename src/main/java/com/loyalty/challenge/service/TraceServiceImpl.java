package com.loyalty.challenge.service;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import com.loyalty.challenge.annotation.TransactionRecorder;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.Tracking;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

/**
 * Interacs with multiple services to create a {@link Tracking}.
 *
 * @author Lucas Kaseta
 */

@Service
public class TraceServiceImpl implements TraceService {
	
	private CountryService countryService;
	private TrackingService trackingService;
	private IpService ipService;
	
	public TraceServiceImpl(CountryService countryService, TrackingService trackingService, IpService ipService) {
		this.countryService = countryService;
		this.trackingService = trackingService;
		this.ipService = ipService;
	}
	
	@Override
	@TransactionRecorder
	@RateLimiter(name = "traceServiceRateLimiter")
	public Tracking traceIpAddress(String ip) {
		Tracking tracking = this.trackingService.findTrackingByIp(ip);
		
		if (tracking == null) {
			tracking = new Tracking();
			tracking.setIp(ip);
		}
		
		if (tracking.getCountry() == null) {
			String countryCode = this.ipService.findCountryCodeByIp(ip);
			Country country = this.countryService.findCountryByCode(countryCode);
			tracking.setCountry(country);
			this.trackingService.save(tracking);
		}
		
		tracking.setDate(OffsetDateTime.now());
		return this.trackingService.fillCurrencyExchange(tracking);
	}
}
