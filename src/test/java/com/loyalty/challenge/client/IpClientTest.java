package com.loyalty.challenge.client;

import static org.junit.Assert.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.challenge.constant.ClientConstants;
import com.loyalty.challenge.dto.CountryDTO;
import com.loyalty.challenge.exception.ExternalResourceException;

@AutoConfigureWebClient(registerRestTemplate = true)
@RestClientTest(IpClient.class)
class IpClientTest {
	
	@Autowired
	private MockRestServiceServer mockRestServiceServer;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private IpClient ipClient;
	
	@Test
	void ipClient_notFoundClientShouldThrowException() {
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(ClientConstants.IP_TO_COUNTRY_URI);
		String url = urlBuilder.build("10.0.0.0").toString();
		this.mockRestServiceServer.expect(requestTo(url)).andRespond(withStatus(HttpStatus.NOT_FOUND));
		
		assertThrows(ExternalResourceException.class, () -> this.ipClient.call(("10.0.0.0")));
	}
	
	@Test
	void ipClient_emptyResponseShouldThrowResourceNotFoundException() throws JsonProcessingException {
		UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(ClientConstants.IP_TO_COUNTRY_URI);
		String url = urlBuilder.build("10.0.0.0").toString();
		
		CountryDTO countryDTO = new CountryDTO();
		this.mockRestServiceServer.expect(requestTo(url)).andRespond(withSuccess(objectMapper.writeValueAsBytes(countryDTO), MediaType.APPLICATION_JSON));
		
		assertThrows(ResourceNotFoundException.class, () -> this.ipClient.call("10.0.0.0"));
	}
}
