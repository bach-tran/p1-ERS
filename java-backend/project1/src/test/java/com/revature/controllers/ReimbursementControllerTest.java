package com.revature.controllers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.NotAllowedException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.exceptions.UnexpectedRequestParametersException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;

public class ReimbursementControllerTest {

	private HttpServletRequest req;
	private HttpServletResponse resp;
	private HttpSession session;
	private ReimbursementController controller;
	
	private AuthService authService;
	private ReimbursementService reimbService;
	private ObjectMapper mapper;
	
	@Before
	public void setUp() throws Exception {
		authService = mock(AuthService.class);
		reimbService = mock(ReimbursementService.class);
		mapper = mock(ObjectMapper.class);
		session = mock(HttpSession.class);
		
		req = mock(HttpServletRequest.class);
		resp = mock(HttpServletResponse.class);
		
		controller = new ReimbursementController(authService, reimbService, mapper);
	}

	@Test
	public void testGETZeroAdditionalPortions() throws InvalidMethodException, NotAuthorizedException, SQLException, IOException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		PrintWriter writer = mock(PrintWriter.class);
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		when(reimbService.getAllReimbursements()).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_png() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_jpeg() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.jpeg");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_jpg() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.jpg");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_gif() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.gif");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testPOSTZeroAdditionalPortions_invalidFileExtension() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.bmp");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testPOSTZeroAdditionalPortions_invalidAmount() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56adsfd");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testPOSTZeroAdditionalPortions_invalidTypeId() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10adfd");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_successfulTrimAmount() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("   50.56     ");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("10");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_successfulTrimTypeId() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("50.56");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("   10      ");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPOSTZeroAdditionalPortions_successfulTrimBoth() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(partFile);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("   50.56      ");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("   10      ");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testPOSTZeroAdditionalPortions_NullPartFile() throws IOException, ServletException, InvalidMethodException, NotAuthorizedException, SQLException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList();
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		
		Part partAmount = mock(Part.class);
		Part partDescription = mock(Part.class);
		Part partTypeId = mock(Part.class);
		Part partFile = mock(Part.class);
		
		when(req.getPart(eq("amount"))).thenReturn(partAmount);
		when(req.getPart(eq("description"))).thenReturn(partDescription);
		when(req.getPart(eq("type"))).thenReturn(partTypeId);
		when(req.getPart(eq("file"))).thenReturn(null);
		
		when(reimbService.partToString(eq(partDescription))).thenReturn("Hello");
		
		when(reimbService.partToString(eq(partAmount))).thenReturn("   50.56      ");
		when(reimbService.partToString(eq(partTypeId))).thenReturn("   10      ");
		
		when(reimbService.partToByteArray(eq(partFile))).thenReturn("test".getBytes());
		
		when(partFile.getSubmittedFileName()).thenReturn("test.png");
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		controller.process(req, resp, portions);
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testPortionsOne_invalidId() throws InvalidMethodException, NotAuthorizedException, SQLException, IOException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10w");
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testGETOnePortion() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=ResourceNotAvailableException.class)
	public void testGETOnePortion_wrongIdURI() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("100");
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testGETOnePortion_employeeNotAuthorOfReimb() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
	}
	
	@Test
	public void testGETOnePortion_managerNotAuthorOfReimb() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		PrintWriter writer = mock(PrintWriter.class);
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test
	public void testPUTOnePortion_approve() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("PUT");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(req.getParameter(eq("operation"))).thenReturn("approve");
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		controller.process(req, resp, portions);
		
		verify(reimbService, times(1)).approveReimbursementById(10, 2);
	}
	
	@Test
	public void testPUTOnePortion_deny() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("PUT");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(req.getParameter(eq("operation"))).thenReturn("deny");
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		controller.process(req, resp, portions);
		
		verify(reimbService, times(1)).denyReimbursementById(10, 2);
	}
	
	@Test(expected=UnexpectedRequestParametersException.class)
	public void testPUTOnePortion_invalidOperation() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("PUT");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(req.getParameter(eq("operation"))).thenReturn("invalidoperation");
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=UnexpectedRequestParametersException.class)
	public void testPUTOnePortion_nullOperationParameter() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("10");
		when(req.getMethod()).thenReturn("PUT");
		when(req.getSession(eq(false))).thenReturn(session);
		
		when(reimbService.getReimbursementById(10)).thenReturn(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING")));
		
		when(req.getParameter(eq("operation"))).thenReturn(null);
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testGETtwoPortions() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("user", "10");
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		when(reimbService.getAllReimbursementsByUserId(10)).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		
		PrintWriter writer = mock(PrintWriter.class);
		
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=InvalidURIPatternException.class)
	public void testGETtwoPortions_invalidFirstPortion() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("invalid", "10");
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		when(reimbService.getAllReimbursementsByUserId(10)).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		
		PrintWriter writer = mock(PrintWriter.class);
		
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	@Test(expected=InvalidMethodException.class)
	public void testGETtwoPortions_invalidMethod() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("user", "10");
		
		when(req.getMethod()).thenReturn("POST");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")));
		
		when(reimbService.getAllReimbursementsByUserId(1)).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		
		PrintWriter writer = mock(PrintWriter.class);
		
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
		
		verify(resp, times(1)).setStatus(eq(200));
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testGETtwoPortions_UserTryingtoAccessAnotherUser() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("user", "10");
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		when(reimbService.getAllReimbursementsByUserId(10)).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		
		PrintWriter writer = mock(PrintWriter.class);
		
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
	
	@Test
	public void testGETtwoPortions_UserTryingtoAccessTheirOwn() throws SQLException, IOException, InvalidMethodException, NotAuthorizedException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		List<String> portions = Arrays.asList("user", "3");
		
		when(req.getMethod()).thenReturn("GET");
		when(req.getSession(eq(false))).thenReturn(session);
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		when(reimbService.getAllReimbursementsByUserId(3)).thenReturn(Arrays.asList(new Reimbursement(1, 50.2, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "Teambuilding Dinner", null, new User(3, "bach_tran", "test", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")),
				new User(2, "john_doe", "test", "John", "Doe", "john_doe@outlook.com", new Role(2, "MANAGER")), new Status(1, "PENDING"), new Type(1, "LODGING"))));
		
		PrintWriter writer = mock(PrintWriter.class);
		
		when(resp.getWriter()).thenReturn(writer);
		
		controller.process(req, resp, portions);
	}
}
