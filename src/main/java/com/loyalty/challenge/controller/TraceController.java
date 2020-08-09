package com.loyalty.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.loyalty.challenge.dto.HistoricalDistanceInvocationDTO;
import com.loyalty.challenge.dto.IpDTO;
import com.loyalty.challenge.dto.TrackingDTO;
import com.loyalty.challenge.facade.TraceFacade;

/**
 * Application endpoints and parameters definition.
 *
 * @author Lucas Kaseta
 */

@RestController
@Validated
public class TraceController {
	
	private TraceFacade traceFacade;
	
	public TraceController(TraceFacade traceFacade) {
		this.traceFacade = traceFacade;
	}
	
	@PostMapping({"/trace"})
	public ResponseEntity<Object> add(@RequestBody IpDTO ipDTO, BindingResult bindingResult) {
		TrackingDTO tracking = this.traceFacade.traceIpAddress(ipDTO, bindingResult);
		return ResponseEntity.ok(tracking);
	}
	
	@GetMapping({"/stats"})
	public ResponseEntity<Object> stats() {
		HistoricalDistanceInvocationDTO historicalDistanceInvocationDTO = this.traceFacade.getHistoricalDistanceInvocation();
		return ResponseEntity.ok(historicalDistanceInvocationDTO);
	}
}
