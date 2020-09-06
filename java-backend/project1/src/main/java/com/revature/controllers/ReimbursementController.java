package com.revature.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.models.Role;
import com.revature.services.AuthService;

public class ReimbursementController {
	
	private Logger log = Logger.getLogger(ReimbursementController.class);
	private HttpSession session;
	private AuthService authService;
	
	public ReimbursementController() {
		super();
		this.authService = new AuthService();
	}
	
	public ReimbursementController(AuthService authService) {
		this.authService = authService;
	}

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, NotAuthorizedException {
		log.info("Processing request");
		
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			if (method.equals("GET")) { 
				session = req.getSession(false);
				
				this.authService.guard(session, new Role[] {new Role(2, "MANAGER")});
				
			} else {
				throw new InvalidMethodException("/reimb/ does not support " + req.getMethod());
			}
		}
	}
	
}
