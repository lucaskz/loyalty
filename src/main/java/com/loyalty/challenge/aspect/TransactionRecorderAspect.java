package com.loyalty.challenge.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import com.loyalty.challenge.annotation.TransactionRecorder;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.service.TransactionHistoryService;

/**
 * Aspect triggered after a trace is made. Records historical traces for later use.
 *
 * @author Lucas Kaseta
 */

@Aspect
@Configuration
public class TransactionRecorderAspect {
	
	private TransactionHistoryService transactionHistoryService;
	
	public TransactionRecorderAspect(TransactionHistoryService transactionHistoryService) {
		this.transactionHistoryService = transactionHistoryService;
	}
	
	@AfterReturning(pointcut = "@annotation(transactionRecorder)", returning = "result")
	public void afterMethodService(TransactionRecorder transactionRecorder, Tracking result) {
		this.transactionHistoryService.recordTransaction(result);
	}
	
}
