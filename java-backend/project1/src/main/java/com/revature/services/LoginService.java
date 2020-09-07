package com.revature.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.revature.dao.UserDAO;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.models.User;
import com.revature.utilities.ConnectionUtility;

public class LoginService {

	private UserDAO dao;
	private Logger log = Logger.getLogger(LoginService.class);
	
	public LoginService() {
		super();
		dao = new UserDAO(ConnectionUtility.getConnection());
	}
	
	public LoginService(UserDAO dao) {
		super();
		this.dao = dao;
	}
	
	public User login(String username, String password) {
		username = username.trim();
		
		try {
			User user = dao.getUserByUsername(username);
			
			if (user == null) {
				return null;
			}
						
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
			
		} catch (SQLException e) {
			log.error("An issue with SQL occurred: " + e.getMessage());
			return null;
		}
		
		return null;
	}
	
	public Map<String, String> parseLoginBody(BufferedReader reader) throws UnexpectedRequestBodyException, IOException {
		
		String body = reader.lines().collect(Collectors.joining());
		
		ObjectMapper om = new ObjectMapper();
		ObjectNode node = om.readValue(body, ObjectNode.class);
		
		if (node.get("username") == null || node.get("password") == null) {
			throw new UnexpectedRequestBodyException("Unexpected body of login request was provided");
		}
		
		JsonNode usernameNode = node.get("username");
		JsonNode passwordNode = node.get("password");
		
		String username = usernameNode.asText();
		String password = passwordNode.asText();
		
		Map<String, String> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		
		return map;
	}
	
	public String hashPassword(String password, MessageDigest digest) throws LoginException {
		try {
	        byte[] hashedBytes = digest.digest(password.getBytes("UTF-8"));
	        
	        return Hex.encodeHexString(hashedBytes);
	    } catch (UnsupportedEncodingException ex) {
	        throw new LoginException("Could not generate hash from String");
	    }
	}
	
}
