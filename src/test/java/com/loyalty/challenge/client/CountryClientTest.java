package com.loyalty.challenge.client;

import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.exception.ExternalResourceException;

@AutoConfigureWebClient(registerRestTemplate = true)
@RestClientTest(CountryClient.class)
class CountryClientTest {
	
	@Autowired
	private MockRestServiceServer mockRestServiceServer;
	
	@Autowired
	private CountryClient countryClient;
	
	@Test
	void countryClient_notFoundClientShouldThrowException() {
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(ClientConstants.REST_COUNTRIES_URI);
		String url = urlBuilder.build("AR").toString();
		
		this.mockRestServiceServer.expect(requestTo(url)).andRespond(withStatus(HttpStatus.NOT_FOUND));
		
		assertThrows(ExternalResourceException.class, () -> this.countryClient.call("AR"));
	}
}
