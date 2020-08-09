package com.loyalty.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.loyalty.challenge.model.TransactionRecord;

public interface TransactionHistoryRepository extends JpaRepository<TransactionRecord, String> {
	
	boolean existsByCountryCode(String countryCode);
	
	@Modifying
	@Query("update TransactionRecord t set t.invocations = t.invocations + 1 where t.countryCode = :countryCode")
	void updateTransactionRecordInvocations(@Param("countryCode") String countryCode);
	
	@Query("select SUM(distance * invocations) / sum(invocations) as averageDistance, min(distance) as minDistance, max(distance) as maxDistance from TransactionRecord ")
	HistoricalDistanceInvocationProjection getHistoricalDistanceInvocation();
}
