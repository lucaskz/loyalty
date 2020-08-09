package com.loyalty.challenge.service;

import com.loyalty.challenge.model.Tracking;

public interface TrackingRepositoryService extends FallbackService<String, Tracking> {
	
	Tracking findTopByIp(String ip);
	
	Tracking save(Tracking tracking);
	
}
