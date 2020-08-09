package com.loyalty.challenge.client;

import static com.loyalty.challenge.constant.CurrencyConstants.BASE_CURRENCY_EXCHANGE;
import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.dto.ErrorDTO;
import com.loyalty.challenge.dto.ExchangeRateDTO;
import com.loyalty.challenge.exception.CurrencyClientException;

@AutoConfigureWebClient(registerRestTemplate = true)
@RestClientTest(CurrencyClient.class)
class CurrencyClientTest {
	
	@Autowired
	private MockRestServiceServer mockRestServiceServer;
	
	@Autowired
	private CurrencyClient currencyClient;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void currencyClient_invalidCurrencyCodeShouldThrowCurrencyClientException() throws JsonProcessingException {
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(ClientConstants.DATA_FIXER_URI);
		String url = urlBuilder.build("test", BASE_CURRENCY_EXCHANGE, "DAC").toString();
		
		ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setType("invalid currency code");
		exchangeRateDTO.setSuccess(false);
		exchangeRateDTO.setError(errorDTO);
		
		this.mockRestServiceServer.expect(requestTo(url)).andRespond(withSuccess(objectMapper.writeValueAsBytes(exchangeRateDTO), MediaType.APPLICATION_JSON));
		
		assertThrows(CurrencyClientException.class, () -> this.currencyClient.call("DAC"));
	}
}
