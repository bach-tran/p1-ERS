package com.revature.services;

import javax.servlet.http.HttpSession;

import com.revature.exceptions.NotAuthorizedException;
import com.revature.models.Role;
import com.revature.models.User;

public class AuthService {

	public AuthService() {
		super();
	}
	
	// Guard based on role
	public void guard(HttpSession session, Role...roles) throws NotAuthorizedException {
		if (session == null || session.getAttribute("currentUser") == null) {
			throw new NotAuthorizedException("No session was found");
		}
		
		User user = (User) session.getAttribute("currentUser");
		for (Role role : roles) {
			if (user.getRole().equals(role)) {
				return;
			}
		}
		
		throw new NotAuthorizedException("User did not meet role criteria: " + roles);
	}
	
}
