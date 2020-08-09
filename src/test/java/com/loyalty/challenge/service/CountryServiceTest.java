package com.loyalty.challenge.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import com.loyalty.challenge.client.CountryClient;
import com.loyalty.challenge.model.Country;
import com.loyalty.challenge.model.Currency;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CountryServiceTest {
	
	@Autowired
	private CountryService countryService;
	
	@InjectMocks
	private CountryServiceImpl countryServiceMock;
	
	@Mock
	private CountryClient countryClientMock;
	
	@Mock
	private CurrencyService currencyServiceMock;
	
	@Mock
	private CountryRepositoryServiceImpl countryRepositoryServiceMock;
	
	@Test
	void countryService_shouldFindCountryFromRepo() {
		this.countryService.save(getCountry("AR"));
		Country country = this.countryService.findCountryByCode("AR");
		
		assertNotNull(country);
	}
	
	@Test
	void countryService_shouldCreateCountryFromClient() {
		Mockito.when(this.countryClientMock.call("DE")).thenReturn(getCountry("DE"));
		Mockito.when(this.countryRepositoryServiceMock.findTopByCountryCode("DE")).thenReturn(null);
		Country country = this.countryServiceMock.findCountryByCode("DE");
		
		assertNotNull(country);
		verify(this.countryClientMock, times(1)).call("DE");
	}
	
	@Test
	void countryService_shouldNotPersistCountryIfCurrencyNotSaved() {
		Country countryWithCurrencies = getCountryWithCurrencies("AR");
		countryWithCurrencies.getCurrencies().forEach(currency -> when(this.currencyServiceMock.save(currency)).thenReturn(currency));
		Mockito.when(this.countryClientMock.call("DE")).thenReturn(getCountry("DE"));
		this.countryServiceMock.save(countryWithCurrencies);
		
		verify(this.currencyServiceMock, times(1)).save(any());
		verify(this.countryRepositoryServiceMock, times(0)).save(any());
	}
	
	@Test
	void countryService_shouldPersistCurrencyAndCountry() {
		this.countryService.save(getCountryWithCurrencies("NZ"));
		
		Country country = this.countryService.findCountryByCode("NZ");
		assertNotNull(country.getId());
		country.getCurrencies().forEach(currency -> assertNotNull(currency.getId()));
	}
	
	private Country getCountryWithCurrencies(String countryCode) {
		Country country = getCountry(countryCode);
		Currency currency = new Currency();
		currency.setCode("ARS");
		country.getCurrencies().add(currency);
		return country;
	}
	
	private Country getCountry(String countryCode) {
		Country country = new Country();
		country.setCountryCode(countryCode);
		country.setCurrencies(new ArrayList<>());
		return country;
	}
	
}
