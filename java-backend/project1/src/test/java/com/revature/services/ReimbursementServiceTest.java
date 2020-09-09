package com.revature.services;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;

import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.UserDAO;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;

public class ReimbursementServiceTest {

	private ReimbursementDAO reimbDao;
	private UserDAO userDao;
	private ReimbursementService reimbService;
	
	@Before
	public void setUp() {
		reimbDao = mock(ReimbursementDAO.class);
		userDao = mock(UserDAO.class);
		reimbService = new ReimbursementService(reimbDao, userDao);
	}
	
	@Test
	public void testGetAllReimbursements() throws SQLException {
				
		when(reimbDao.getAllReimbursements()).thenReturn(Arrays.asList(
				new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING"))));
		
		assertEquals(Arrays.asList(new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING"))), reimbService.getAllReimbursements());
	}
	
	@Test
	public void testGetAllReimbursementsByUserId() throws SQLException {
		when(reimbDao.getAllReimbursementsByUserId(eq(1))).thenReturn(Arrays.asList(
				new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING"))));
		
		assertEquals(Arrays.asList(new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING"))), reimbService.getAllReimbursementsByUserId(1));
	}
	
	@Test
	public void testGetReimbursementById() throws SQLException {
		when(reimbDao.getReimbursementById(eq(12))).thenReturn(new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING")));
		
		assertEquals(new Reimbursement(12, 343, new Timestamp(1599531276427L), new Timestamp(1599539986800L), "dfdfdfsdfsf", null, 
						new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")), 
						new User(2, "john_doe", "b822f1cd2dcfc685b47e83e3980289fd5d8e3ff3a82def24d7d1d68bb272eb32", "John", "Doe", "john_doe@company.com", new Role(2, "MANAGER")), 
						new Status(3, "DENIED"), 
						new Type(1, "LODGING")), reimbService.getReimbursementById(12));
	}
	
	@Test
	public void testApproveReimbursementByIdSuccess() throws SQLException {
		when(reimbDao.approveReimbursementById(eq(16), eq(2))).thenReturn(true);
		
		assertTrue(reimbService.approveReimbursementById(16, 2));
	}
	
	@Test
	public void testApproveReimbursementByIdNotSuccessful() throws SQLException {
		when(reimbDao.approveReimbursementById(eq(16), eq(2))).thenReturn(true);
		
		assertFalse(reimbService.approveReimbursementById(16, 1));
	}
	
	@Test
	public void testDenyReimbursementByIdSuccess() throws SQLException {
		when(reimbDao.denyReimbursementById(eq(16), eq(2))).thenReturn(true);
		
		assertTrue(reimbService.denyReimbursementById(16, 2));
	}
	
	@Test
	public void testDenyReimbursementByIdNotSuccessful() throws SQLException {
		when(reimbDao.denyReimbursementById(eq(16), eq(2))).thenReturn(true);
		
		assertFalse(reimbService.denyReimbursementById(16, 1));
	}
	
	@Test
	public void addReimbursementSuccess() throws SQLException {
		when(reimbDao.addReimbursement(anyDouble(), anyString(), AdditionalMatchers.or(AdditionalMatchers.gt(0), AdditionalMatchers.lt(5)), any(byte[].class), anyInt())).thenReturn(true);
		
		assertTrue(reimbService.addReimbursement(100.5, "Hello", 3, new byte[] {}, 1));
	}
	
	@Test
	public void addReimbursementFail_InvalidType1() throws SQLException {
		when(reimbDao.addReimbursement(anyDouble(), anyString(), AdditionalMatchers.and(AdditionalMatchers.gt(0), AdditionalMatchers.lt(5)), any(byte[].class), anyInt())).thenReturn(true);
		
		assertFalse(reimbService.addReimbursement(100.5, "Hello", 0, new byte[] {}, 1));
	}
	
	@Test
	public void addReimbursementFail_InvalidType2() throws SQLException {
		when(reimbDao.addReimbursement(anyDouble(), anyString(), AdditionalMatchers.and(AdditionalMatchers.gt(0), AdditionalMatchers.lt(5)), any(byte[].class), anyInt())).thenReturn(true);
		
		assertFalse(reimbService.addReimbursement(100.5, "Hello", 5, new byte[] {}, 1));
	}
	
	@Test
	public void testPartToString() throws IOException {
		Part part = mock(Part.class);
		
		InputStream stream = new ByteArrayInputStream("test".getBytes());
		
		when(part.getInputStream()).thenReturn(stream);
		
		String expected = "test";
		
		assertEquals("test", reimbService.partToString(part));
	}
	
	@Test
	public void testPartToByteArray() throws IOException {
		Part part = mock(Part.class);
		
		// Make part a 'String' value
		InputStream stream = new ByteArrayInputStream("test".getBytes());
		when(part.getInputStream()).thenReturn(stream);
		
		byte[] expected = "test".getBytes();
		
		assertTrue(Arrays.equals(expected, reimbService.partToByteArray(part)));
	}

}
