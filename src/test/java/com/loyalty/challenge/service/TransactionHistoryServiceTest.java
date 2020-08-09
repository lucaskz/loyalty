package com.loyalty.challenge.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.HistoricalDistanceInvocation;
import com.loyalty.challenge.model.Tracking;
import com.loyalty.challenge.repository.TransactionHistoryRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TransactionHistoryServiceTest {
	
	@Mock
	private TransactionHistoryRepository transactionHistoryRepositoryMock;
	
	private TransactionHistoryService transactionHistoryServiceMock;
	
	@Autowired
	private TransactionHistoryService transactionHistoryService;
	
	private Tracking tracking;
	
	@BeforeEach
	void setup() {
		this.transactionHistoryServiceMock = new TransactionHistoryService(transactionHistoryRepositoryMock);
		this.tracking = getTracking("AR", 100.0, "Argentina");
	}
	
	@Test
	void transactionHistoryService_emptyRecords() {
		HistoricalDistanceInvocation historicalDistanceInvocation = this.transactionHistoryService.getHistoricalDistanceInvocation();
		assertNull(historicalDistanceInvocation.getAverageDistance());
		assertNull(historicalDistanceInvocation.getAverageDistance());
		assertNull(historicalDistanceInvocation.getAverageDistance());
	}
	
	@Test
	void transactionHistoryService_historicalDataCalc() {
		Tracking argentineTrack = getTracking("AR", 10, "Argentina");
		Tracking uyTrack = getTracking("UY", 5, "Uruguay");
		
		this.transactionHistoryService.recordTransaction(uyTrack);
		this.transactionHistoryService.recordTransaction(uyTrack);
		this.transactionHistoryService.recordTransaction(argentineTrack);
		
		HistoricalDistanceInvocation historicalDistanceInvocation = this.transactionHistoryService.getHistoricalDistanceInvocation();
		assertEquals(historicalDistanceInvocation.getMaxDistance(), 10.0, 0.1);
		assertEquals(historicalDistanceInvocation.getMinDistance(), 5.0, 0.1);
		assertEquals(historicalDistanceInvocation.getAverageDistance(), 6.666666667, 0.000000001);
	}
	
	@Test
	void updateTransactionRecordInvocations_shouldBeCalled() {
		Mockito.when(transactionHistoryRepositoryMock.existsByCountryCode(any())).thenReturn(true);
		this.transactionHistoryServiceMock.recordTransaction(this.tracking);
		
		String countryCode = this.tracking.getCountry().getCountryCode();
		verify(this.transactionHistoryRepositoryMock, times(1)).existsByCountryCode(countryCode);
		verify(this.transactionHistoryRepositoryMock, times(1)).updateTransactionRecordInvocations(countryCode);
		verify(this.transactionHistoryRepositoryMock, times(0)).save(any());
	}
	
	@Test
	void historyRepositorySave_shouldBeCalled() {
		Mockito.when(transactionHistoryRepositoryMock.existsByCountryCode(any())).thenReturn(false);
		this.transactionHistoryServiceMock.recordTransaction(this.tracking);
		
		String countryCode = this.tracking.getCountry().getCountryCode();
		verify(this.transactionHistoryRepositoryMock, times(1)).existsByCountryCode(countryCode);
		verify(this.transactionHistoryRepositoryMock, times(0)).updateTransactionRecordInvocations(countryCode);
		verify(this.transactionHistoryRepositoryMock, times(1)).save(any());
	}
	
	private Tracking getTracking(String countryCode, double distance, String countryName) {
		Tracking tracking = new Tracking();
		tracking.setCountry(getCountry(countryCode, distance, countryName));
		return tracking;
	}
	
	private Country getCountry(String countryCode, double distance, String countryName) {
		Country country = new Country();
		country.setCountryCode(countryCode);
		country.setDistance(distance);
		country.setCountryName(countryName);
		return country;
	}
}
