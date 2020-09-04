package com.revature.services;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.revature.controllers.LoginController;
import com.revature.dao.UserDAO;
import com.revature.models.User;
import com.revature.utilities.ConnectionUtility;

public class LoginService {

	private Logger log = Logger.getLogger(LoginService.class);
	
	public LoginService() {
		super();
	}
	
	public User login(String username, String password) {
		UserDAO dao = new UserDAO(ConnectionUtility.getConnection());
		
		username = username.trim();
		
		try {
			User user = dao.getUserByUsername(username);
						
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
			
		} catch (SQLException e) {
			log.error("An issue with SQL occurred: " + e.getMessage());
			return null;
		}
		
		return null;
	}
	
	// Implement password hashing
	// Test password hashing
	
	// Tests
	
	// Test username with 1. trailing and 
	// 	2. leading whitespaces
	
	// Test invalid username and correct password
	
	// Test invalid password and correct username
	
	// Test invalid username and password
	
	// Test correct username and password
}
