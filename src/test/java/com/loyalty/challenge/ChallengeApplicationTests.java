package com.loyalty.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loyalty.challenge.dto.IpDTO;

@SpringBootTest
@AutoConfigureMockMvc
class ChallengeApplicationTests {
	
	@Autowired
	private MockMvc mvc;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	void whenInvalidGetStatsShouldReturnNotFound() throws Exception {
		testGetMethod("/stat", 404);
	}
	
	@Test
	void whenValidGetStatsShouldReturnOk() throws Exception {
		testGetMethod("/stats", 200);
	}
	
	@Test
	void whenInvalidInputShouldReturnBadRequest() throws Exception {
		IpDTO ipDTO = new IpDTO();
		ipDTO.setIp("10.2.3.invalid");
		testPostWithBody("/trace", ipDTO, 400);
	}
	
	@Test
	void whenInvalidPostUrlShouldReturnNotFound() throws Exception {
		IpDTO ipDTO = new IpDTO();
		ipDTO.setIp("62.36.4.115");
		testPostWithBody("/trac", ipDTO, 404);
	}
	
	private void testGetMethod(String uri, int expectedStatus) throws Exception {
		MockHttpServletResponse httpResponse = makeCallToService(uri);
		
		assertEquals(expectedStatus, httpResponse.getStatus());
	}
	
	private void testPostWithBody(String uri, Object bodyObject, int expectedStatus) throws Exception {
		MockHttpServletResponse httpServletResponse = makeCallToService(uri, mapToJson(bodyObject));
		
		assertEquals(expectedStatus, httpServletResponse.getStatus());
	}
	
	private MockHttpServletResponse makeCallToService(String uri) throws Exception {
		return mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn().getResponse();
	}
	
	private MockHttpServletResponse makeCallToService(String uri, String body) throws Exception {
		return mvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(body))
				.andReturn().getResponse();
	}
	
	private String mapToJson(Object body) throws JsonProcessingException {
		return objectMapper.writeValueAsString(body);
	}
}
