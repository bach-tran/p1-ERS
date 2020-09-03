package com.revature.controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.ReadRequestException;

public class LoginControllerTest {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	@Before
	public void setUp() {
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testAdditionalURIPortions() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException {
		LoginController controller = new LoginController();
		
		List<String> portions = Arrays.asList("50", "test");
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testZeroPortions() throws LoginException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("SOMEMETHOD");
		
		
		try {
			controller.process(req, resp, portions);
		} catch (InvalidMethodException | InvalidURIPatternException | ReadRequestException e) { }
		
		verify(req, times(1)).getMethod();
	}
	
	@Test(expected=NullPointerException.class)
	public void testPostHTTPMethod() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, IOException, LoginException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		
		controller.process(req, resp, portions);
		
		verify(req, times(1)).getMethod();
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testDifferentHTTPMethod() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("GET");
		
		controller.process(req, resp, portions);
	}
	
	// parsePostBody Method
	@Test
	public void testParsePostBody() throws IOException {
		LoginController controller = new LoginController();
		BufferedReader br = mock(BufferedReader.class, RETURNS_DEEP_STUBS);
		
		ObjectMapper om = mock(ObjectMapper.class);
		ObjectNode node = mock(ObjectNode.class);
		JsonNode usernameNode = mock(JsonNode.class);
		JsonNode passwordNode = mock(JsonNode.class);
		
		when(req.getReader()).thenReturn(br);
		when(br.lines().collect(any())).thenReturn("{\"username\":\"test\",\"password\":\"test\"}");
		when(om.readValue("{\"username\":\"test\",\"password\":\"test\"}", ObjectNode.class)).thenReturn(node);
		when(node.get("username")).thenReturn(usernameNode);
		when(node.get("password")).thenReturn(passwordNode);
		when(usernameNode.asText()).thenReturn("test");
		when(passwordNode.asText()).thenReturn("test");
		
		Map<String, String> map = new HashMap<>();
		map.put("username", "test");
		map.put("password", "test");
		assertEquals(map, controller.parsePostBody(req));
	}

}
