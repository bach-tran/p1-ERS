package com.revature.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.models.User;

public class LogoutController {
	
	private Logger log = Logger.getLogger(LogoutController.class);
	private HttpSession session;

	public LogoutController() {
		super();
	}
	
	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidURIPatternException, InvalidMethodException {
		log.info("Processing request");
		
		if (portions.size() == 0) {
			
			if (!req.getMethod().equals("GET")) {
				throw new InvalidMethodException("/logout/ only accepts GET requests. Another request type was received.");
			}
			
			session = req.getSession(false);
			if (session != null && session.getAttribute("currentUser") != null) {
				User currentUser = (User) session.getAttribute("currentUser");
				String username = currentUser.getUsername();
				session.invalidate();
				log.info(username + " successfully logged out");
			} else {
				log.info("A request to logout was made, but no session is active");
			}
			
		} else {
			throw new InvalidURIPatternException("Invalid URI pattern sent to LogoutController");
		}
		
	}
	
}
