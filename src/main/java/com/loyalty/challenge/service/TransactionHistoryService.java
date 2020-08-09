package com.loyalty.challenge.service;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.loyalty.challenge.configuration.ThreadConfiguration;
import com.loyalty.challenge.model.HistoricalDistanceInvocation;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.model.TransactionRecord;
import com.loyalty.challenge.repository.HistoricalDistanceInvocationProjection;
import com.loyalty.challenge.repository.TransactionHistoryRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class TransactionHistoryService {
	
	private TransactionHistoryRepository transactionHistoryRepository;
	
	public TransactionHistoryService(TransactionHistoryRepository transactionHistoryRepository) {
		this.transactionHistoryRepository = transactionHistoryRepository;
	}
	
	@Transactional
	@Async(ThreadConfiguration.TRANSACTION_RECORDER_THREAD_POOL_TASK_EXECUTOR)
	public void recordTransaction(Tracking tracking) {
		if (transactionHistoryRepository.existsByCountryCode(tracking.getCountry().getCountryCode())) {
			this.transactionHistoryRepository.updateTransactionRecordInvocations(tracking.getCountry().getCountryCode());
		} else {
			TransactionRecord transactionRecord = new TransactionRecord();
			transactionRecord.setCountryCode(tracking.getCountry().getCountryCode());
			transactionRecord.setDistance(tracking.getCountry().getDistance());
			transactionRecord.setCountryName(tracking.getCountry().getCountryName());
			this.transactionHistoryRepository.save(transactionRecord);
		}
	}
	
	@RateLimiter(name = "transactionHistoryServiceRateLimiter")
	public HistoricalDistanceInvocation getHistoricalDistanceInvocation() {
		HistoricalDistanceInvocationProjection projection = this.transactionHistoryRepository.getHistoricalDistanceInvocation();
		HistoricalDistanceInvocation historicalDistanceInvocation = new HistoricalDistanceInvocation();
		historicalDistanceInvocation.setAverageDistance(projection.getAverageDistance());
		historicalDistanceInvocation.setMaxDistance(projection.getMaxDistance());
		historicalDistanceInvocation.setMinDistance(projection.getMinDistance());
		return historicalDistanceInvocation;
	}
}
