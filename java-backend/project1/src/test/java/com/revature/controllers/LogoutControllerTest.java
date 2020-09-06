package com.revature.controllers;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;

public class LogoutControllerTest {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	@Before
	public void setUp() {
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testNoAdditionalURIPortions_InvalidMethod() throws InvalidURIPatternException, InvalidMethodException {
		LogoutController controller = new LogoutController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testNoAdditionalURIPortions_ValidMethod_sessionNotNull() throws InvalidURIPatternException, InvalidMethodException {
		LogoutController controller = new LogoutController();
		List<String> portions = Arrays.asList();
		
		HttpSession session = mock(HttpSession.class);
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(anyBoolean())).thenReturn(session);
		
		controller.process(req, resp, portions);
		
		verify(session, times(1)).invalidate();
	}
	
	@Test
	public void testNoAdditionalURIPortions_ValidMethod_sessionNull() throws InvalidURIPatternException, InvalidMethodException {
		LogoutController controller = new LogoutController();
		List<String> portions = Arrays.asList();
		
		HttpSession session = mock(HttpSession.class);
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(anyBoolean())).thenReturn(null);
		
		controller.process(req, resp, portions);
		
		verify(session, times(0)).invalidate();
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testAdditionalURIPortion_invalid() throws InvalidURIPatternException, InvalidMethodException {
		LogoutController controller = new LogoutController();
		List<String> portions = Arrays.asList("anotherportion");
		
		controller.process(req, resp, portions);
	}

}
