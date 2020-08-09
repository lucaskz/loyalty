package com.loyalty.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.Currency;
import com.loyalty.challenge.model.Tracking;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TrackingServiceTest {
	
	@Mock
	private TrackingRepositoryService trackingRepositoryServiceMock;
	
	@Mock
	private CountryService countryServiceMock;
	
	@Mock
	private CurrencyService currencyServiceMock;
	
	@Autowired
	private TrackingServiceImpl trackingService;
	
	@InjectMocks
	private TrackingServiceImpl trackingServiceMock;
	
	@Test
	void currencyFillService_successFill() {
		Map<String, BigDecimal> currencies = new HashMap<>();
		BigDecimal arsQuote = new BigDecimal(100.5);
		BigDecimal usdQuote = new BigDecimal(135.0);
		currencies.put("ARS", arsQuote);
		currencies.put("USD", usdQuote);
		Tracking tracking = getTracking();
		
		Mockito.when(this.currencyServiceMock.getExchangeRate("ARS,USD")).thenReturn(currencies);
		this.trackingServiceMock.fillCurrencyExchange(tracking);
		
		verify(this.currencyServiceMock, times(1)).getExchangeRate("ARS,USD");
		tracking.getCountry().getCurrencies().forEach(c -> assertEquals(c.getQuote(), currencies.get(c.getCode())));
	}
	
	@Test
	void currencyFillService_oneQuoteAvailable() {
		Map<String, BigDecimal> currencies = new HashMap<>();
		BigDecimal currencyQuote = new BigDecimal(100.5);
		currencies.put("ARS", currencyQuote);
		Tracking tracking = getTracking();
		
		Mockito.when(this.currencyServiceMock.getExchangeRate("ARS,USD")).thenReturn(currencies);
		this.trackingServiceMock.fillCurrencyExchange(tracking);
		
		verify(this.currencyServiceMock, times(1)).getExchangeRate("ARS,USD");
		
		assertEquals(currencyQuote, tracking.getCountry().getCurrencies().stream().filter(currency -> currency.getCode().equals("ARS")).findAny().map(Currency::getQuote).orElse(BigDecimal.ZERO));
	}
	
	@Test
	void trackingService_cascadePersistenceOk() {
		Tracking tracking = getTracking();
		String ip = "10.0.0.0";
		tracking.setIp(ip);
		this.trackingService.save(tracking);
		
		Tracking t = this.trackingService.findTrackingByIp(ip);
		
		assertNotNull(t);
		assertNotNull(t.getCountry());
		assertNotNull(t.getCountry().getCurrencies());
	}
	
	@Test
	void countryService_saveShouldNotBeCalled() {
		ObjectId objectId = new ObjectId();
		Tracking tracking = getTracking(objectId);
		
		this.trackingServiceMock.save(tracking);
		verify(this.trackingRepositoryServiceMock, times(1)).save(tracking);
		verify(this.countryServiceMock, times(0)).save(tracking.getCountry());
	}
	
	private Tracking getTracking() {
		Tracking tracking = new Tracking();
		tracking.setCountry(getCountry());
		return tracking;
	}
	
	private Tracking getTracking(ObjectId countryObjectId) {
		Tracking tracking = getTracking();
		tracking.getCountry().setId(countryObjectId);
		return tracking;
	}
	
	private Country getCountry() {
		Country country = new Country();
		country.setCountryCode("AR");
		country.setCurrencies(getCurrencies());
		return country;
	}
	
	private List<Currency> getCurrencies() {
		List<Currency> currencies = new ArrayList<>();
		Currency arsCurrency = new Currency();
		arsCurrency.setCode("ARS");
		currencies.add(arsCurrency);
		Currency usdCurrency = new Currency();
		usdCurrency.setCode(("USD"));
		currencies.add(usdCurrency);
		return currencies;
	}
	
}
