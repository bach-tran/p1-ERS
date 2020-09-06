package com.revature.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ReadRequestException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.LoginService;

public class LoginControllerTest {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession session;
	
	@Before
	public void setUp() {
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
	}
	
	
	@Test
	public void testZeroAdditionalPortions_getMethodRuns() throws LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("SOMEMETHOD");
		
		try {
			controller.process(req, resp, portions);
		} catch (InvalidMethodException | InvalidURIPatternException | ReadRequestException e) { }
		
		verify(req, times(1)).getMethod();
	}
	
	
	@Test(expected=NullPointerException.class)
	public void testZeroAdditionalPortions_validMethod() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, IOException, LoginException, UnexpectedRequestBodyException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		
		controller.process(req, resp, portions);
		
		verify(req, times(1)).getMethod();
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testZeroAdditionalPortions_invalidMethod() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("GET");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=LoginException.class)
	public void testZeroAdditionalPortions_nonNullSession_andNonNullCurrentUser() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testZeroAdditonalPortions_nonNullSession_nullCurrentUser() throws IOException, UnexpectedRequestBodyException, InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, NotAuthorizedException {
		LoginService loginService = mock(LoginService.class);
		LoginController controller = new LoginController(loginService);
		
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(null);
		
		when(loginService.login(any(), any())).thenReturn(new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		when(req.getSession()).thenReturn(session);
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=LoginException.class)
	public void testZeroAdditonalPortions_nonNullSession_nullCurrentUser_nullUserLogin() throws IOException, UnexpectedRequestBodyException, InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, NotAuthorizedException {
		LoginService loginService = mock(LoginService.class);
		LoginController controller = new LoginController(loginService);
		
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(null);
		
		when(loginService.login(any(), any())).thenReturn(null);
		when(req.getSession()).thenReturn(session);
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testZeroAdditionalPortions_nullSession_nullCurrentUser() throws IOException, UnexpectedRequestBodyException, InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, NotAuthorizedException {
		LoginService loginService = mock(LoginService.class);
		LoginController controller = new LoginController(loginService);
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(null);
		
		Map<String, String> map = new HashMap<>();
		map.put("username", "bach_tran");
		map.put("password", "12345");
		
		when(loginService.parseLoginBody(any())).thenReturn(map);
		when(loginService.login(map.get("username"), map.get("password"))).thenReturn(new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		when(req.getSession()).thenReturn(session);
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=LoginException.class)
	public void testZeroAdditionalPortions_nullSession_nullCurrentUser_nullUserLogin() throws IOException, UnexpectedRequestBodyException, InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, NotAuthorizedException {
		LoginService loginService = mock(LoginService.class);
		LoginController controller = new LoginController(loginService);
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(null);
		
		Map<String, String> map = new HashMap<>();
		map.put("username", "bach_tran");
		map.put("password", "12345");
		
		when(loginService.parseLoginBody(any())).thenReturn(map);
		when(loginService.login(map.get("username"), map.get("password"))).thenReturn(null);
		when(req.getSession()).thenReturn(session);
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	// One additional Portion tests /login/check
	@Test(expected=InvalidURIPatternException.class)
	public void testInvalidAdditionalURIPortion() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		
		List<String> portions = Arrays.asList("randompath");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testAdditionalURIPortion_two() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		
		List<String> portions = Arrays.asList("randompath", "anotherrandompath");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testAdditionalURIPortion_check_incorrectmethod() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList("check");
		
		when(req.getMethod()).thenReturn("POST");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=LoginException.class)
	public void testAdditionalURIPortion_check_nullSession() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList("check");
		
		when(req.getSession()).thenReturn(null);
		when(req.getMethod()).thenReturn("GET");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=LoginException.class)
	public void testAdditionalURIPortion_check_nullCurrentUser() throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException, NotAuthorizedException {
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList("check");
		
		when(req.getSession()).thenReturn(session);
		when(req.getMethod()).thenReturn("GET");
		when(req.getAttribute(eq("currentUser"))).thenReturn(null);
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testAdditonalURIPortion_check_noException() throws IOException, InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, NotAuthorizedException {
		PrintWriter writer = mock(PrintWriter.class);
		
		LoginController controller = new LoginController();
		List<String> portions = Arrays.asList("check");
		
		when(req.getSession(eq(false))).thenReturn(session);
		when(req.getMethod()).thenReturn("GET");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}

}
