package com.loyalty.challenge.service;

import com.loyalty.challenge.model.Tracking;

public interface TrackingService extends FallbackService<Tracking, Tracking> {
	
	Tracking fillCurrencyExchange(Tracking tracking);
	
	Tracking save(Tracking tracking);
	
	Tracking findTrackingByIp(String ip);
	
}
