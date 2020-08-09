package com.loyalty.challenge.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import com.loyalty.challenge.dto.HistoricalDistanceInvocationDTO;
import com.loyalty.challenge.dto.IpDTO;
import com.loyalty.challenge.dto.TrackingDTO;
import com.loyalty.challenge.exception.InvalidArgumentException;
import com.loyalty.challenge.model.HistoricalDistanceInvocation;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.service.TraceService;
import com.loyalty.challenge.service.TransactionHistoryService;
import com.loyalty.challenge.transformer.HistoricalDistanceInvocationTransformer;
import com.loyalty.challenge.transformer.TrackingTransformer;
import com.loyalty.challenge.validation.IpValidator;

/**
 * Validates a trace ip request. Also, calls the response transformers for each service.
 *
 * @author Lucas Kaseta
 */

@Component
public class TraceFacade {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TraceFacade.class);
	
	private TraceService traceService;
	private TransactionHistoryService transactionHistoryService;
	private HistoricalDistanceInvocationTransformer historicalDistanceInvocationTransformer;
	private TrackingTransformer trackingTransformer;
	private IpValidator ipValidator;
	
	public TraceFacade(TraceService traceService, TransactionHistoryService transactionHistoryService, HistoricalDistanceInvocationTransformer historicalDistanceInvocationTransformer, TrackingTransformer trackingTransformer, IpValidator ipValidator) {
		this.traceService = traceService;
		this.transactionHistoryService = transactionHistoryService;
		this.historicalDistanceInvocationTransformer = historicalDistanceInvocationTransformer;
		this.trackingTransformer = trackingTransformer;
		this.ipValidator = ipValidator;
	}
	
	public TrackingDTO traceIpAddress(IpDTO ipDTO, BindingResult bindingResult) {
		this.validateInput(ipDTO, bindingResult);
		Tracking tracking = this.traceService.traceIpAddress(ipDTO.getIp());
		return this.trackingTransformer.transform(tracking);
	}
	
	public HistoricalDistanceInvocationDTO getHistoricalDistanceInvocation() {
		HistoricalDistanceInvocation historicalDistanceInvocation = this.transactionHistoryService.getHistoricalDistanceInvocation();
		return this.historicalDistanceInvocationTransformer.transform(historicalDistanceInvocation);
	}
	
	private void validateInput(IpDTO ipDTO, BindingResult bindingResult) {
		this.ipValidator.validate(ipDTO, bindingResult);
		if (bindingResult.hasErrors()) {
			InvalidArgumentException e = new InvalidArgumentException(bindingResult.getAllErrors().stream().findFirst().map(DefaultMessageSourceResolvable::getCode).orElse(null));
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}
}
