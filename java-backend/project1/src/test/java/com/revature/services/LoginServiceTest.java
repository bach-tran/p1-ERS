package com.revature.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.revature.dao.UserDAO;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.models.Role;
import com.revature.models.User;

public class LoginServiceTest {

	// parsePostBody Method
	@Test
	public void testParsePostBody_success() throws IOException, UnexpectedRequestBodyException {
		LoginService loginService = new LoginService();
		
		BufferedReader reader = mock(BufferedReader.class, RETURNS_DEEP_STUBS);
		
		ObjectMapper om = mock(ObjectMapper.class);
		ObjectNode node = mock(ObjectNode.class);
		JsonNode usernameNode = mock(JsonNode.class);
		JsonNode passwordNode = mock(JsonNode.class);

		when(reader.lines().collect(any())).thenReturn("{\"username\":\"test\",\"password\":\"test\"}");
		when(om.readValue("{\"username\":\"test\",\"password\":\"test\"}", ObjectNode.class)).thenReturn(node);
		when(node.get("username")).thenReturn(usernameNode);
		when(node.get("password")).thenReturn(passwordNode);
		when(usernameNode.asText()).thenReturn("test");
		when(passwordNode.asText()).thenReturn("test");
		
		Map<String, String> map = new HashMap<>();
		map.put("username", "test");
		map.put("password", "test");
		assertEquals(map, loginService.parseLoginBody(reader));
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testParsePostBody_nullPassword() throws IOException, UnexpectedRequestBodyException {
		LoginService loginService = new LoginService();
		
		BufferedReader reader = mock(BufferedReader.class, RETURNS_DEEP_STUBS);

		when(reader.lines().collect(any())).thenReturn("{\"username\":\"test\"}");
		
		loginService.parseLoginBody(reader);
	}
	
	@Test(expected=UnexpectedRequestBodyException.class)
	public void testParsePostBody_nullUsername() throws IOException, UnexpectedRequestBodyException {
		LoginService loginService = new LoginService();
		
		BufferedReader reader = mock(BufferedReader.class, RETURNS_DEEP_STUBS);
		
		when(reader.lines().collect(any())).thenReturn("{\"password\":\"test\"}");
		
		loginService.parseLoginBody(reader);
	}
	
	@Test
	public void testLogin_success() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(expectedUser, loginService.login("bach_tran", "12345"));
	}
	
	@Test
	public void testLogin_success_leadingWhitespaceUser() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(expectedUser, loginService.login("    bach_tran", "12345"));
	}
	
	@Test
	public void testLogin_success_trailingWhitespaceUser() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(expectedUser, loginService.login("bach_tran    ", "12345"));
	}
	
	@Test
	public void testLogin_success_leadingTrailingWhitespaceUser() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(expectedUser, loginService.login("   bach_tran    ", "12345"));
	}
	
	@Test
	public void testLogin_null_leadingWhitespacePass() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("bach_tran", "   12345"));
	}
	
	@Test
	public void testLogin_null_trailingWhitespacePass() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("bach_tran", "12345   "));
	}
	
	@Test
	public void testLogin_null_leadingTrailingWhitespacePass() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("bach_tran", "    12345   "));
	}
	
	@Test
	public void testLogin_correctUser_incorrectPassword() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("bach_tran", "incorrectPass"));
	}
	
	@Test
	public void testLogin_incorrectUser_correctPassword() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("incorrectUser", "12345"));
	}
	
	@Test
	public void testLogin_incorrectUser_incorrectPassword() throws SQLException {
		UserDAO dao = mock(UserDAO.class);
		LoginService loginService = new LoginService(dao);
		
		User expectedUser = new User(1, "bach_tran", "12345", "Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE"));
		when(dao.getUserByUsername("bach_tran")).thenReturn(expectedUser);
		
		assertEquals(null, loginService.login("incorrectUser", "incorrectPassword"));
	}
	
	@Test
	public void testHashPassword() throws NoSuchAlgorithmException, LoginException {
		LoginService loginService = new LoginService();
		String expected = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5";
		
		// Reverse from hex string expected output to byte array produced by MessageDigest (which is being mocked)
		String str = "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5";
	    byte[] val = new byte[str.length() / 2];
	    for (int i = 0; i < val.length; i++) {
	    	int index = i * 2;
	    	int j = Integer.parseInt(str.substring(index, index + 2), 16);
	        val[i] = (byte) j;
	    }
		
		MessageDigest digest = mock(MessageDigest.class);
		
		when(digest.digest(any(byte[].class))).thenReturn(val); // return hashed bytes
		
		String actual = loginService.hashPassword("12345", digest);
		assertEquals(expected, actual);
	}

}
