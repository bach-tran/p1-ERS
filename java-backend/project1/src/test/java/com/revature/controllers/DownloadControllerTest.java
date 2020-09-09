package com.revature.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.DownloadService;
import com.revature.services.ReimbursementService;

public class DownloadControllerTest {

	private DownloadService downloadService;
	private AuthService authService;
	private ReimbursementService reimbService;
	private ObjectMapper mapper;
	
	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	private DownloadController controller;
	
	@Before
	public void setUp() throws Exception {
		downloadService = mock(DownloadService.class);
		authService = mock(AuthService.class);
		reimbService = mock(ReimbursementService.class, RETURNS_DEEP_STUBS);
		mapper = mock(ObjectMapper.class);
		
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
		
		controller = new DownloadController(downloadService, authService, reimbService, mapper);
	}

	@Test(expected=InvalidURIPatternException.class)
	public void testNoPortions() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList();
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testReimbPortionNoId() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testReimbPortionInvalidId() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb", "10.5");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testReimbPortionInvalidMethod() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb", "10");
		
		when(req.getMethod()).thenReturn("POST");
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testValidId_Reimbursement_AuthorNotCurrentEmployee() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb", "10");
		
		HttpSession session = mock(HttpSession.class);
		
		when(req.getMethod()).thenReturn("GET");
		
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		when(reimbService.getReimbursementById(10).getAuthor().getId()).thenReturn(10);
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testValidId_Reimbursement_AuthorCurrentEmployee() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb", "10");
		
		HttpSession session = mock(HttpSession.class);
		
		when(req.getMethod()).thenReturn("GET");
		
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		when(reimbService.getReimbursementById(10).getAuthor().getId()).thenReturn(1);
		
		try {
		controller.process(req, resp, portions);
		} catch(NullPointerException e) {}
		
		verify(downloadService, times(1)).getReceipt(eq(10));
	}
	
	@Test
	public void testValidId_Reimbursement_ManagerAccessToOtherUserReimbursements() throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		List<String> portions = Arrays.asList("reimb", "10");
		
		HttpSession session = mock(HttpSession.class);
		
		when(req.getMethod()).thenReturn("GET");
		
		when(req.getSession(eq(false))).thenReturn(session);
		
		// Role of User is new Role(2, "MANAGER")
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(2, "MANAGER")));
		
		// Author ID is 5
		when(reimbService.getReimbursementById(10).getAuthor().getId()).thenReturn(5);
		
		try {
		controller.process(req, resp, portions);
		} catch(NullPointerException e) {}
		
		verify(downloadService, times(1)).getReceipt(eq(10));
	}
}
