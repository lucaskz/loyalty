package com.loyalty.challenge.service;

import com.loyalty.challenge.model.Tracking;

public interface TraceService {
	
	Tracking traceIpAddress(String ip);
}
