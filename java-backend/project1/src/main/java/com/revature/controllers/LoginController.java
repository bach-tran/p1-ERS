package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.ReadRequestException;
import com.revature.models.User;
import com.revature.services.LoginService;
import com.revature.utilities.ConnectionUtility;
import com.revature.utilities.RequestUtility;

public class LoginController {
	
	private Logger log = Logger.getLogger(LoginController.class);
	private HttpSession session;
	private LoginService loginService;
	
	public LoginController() {
		super();
		this.loginService = new LoginService();
	}
	
	public LoginController(LoginService service) {
		this.loginService = service;
	}

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException, IOException {
		log.info("Processing request");
		// /login
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			if (!method.equals("POST")) {
				throw new InvalidMethodException("/login/ only accepts POST requests. Another request type was received.");
			}
			
			session = req.getSession(false);

			if (session == null || session.getAttribute("currentUser") == null) {
				log.info("Session or currentUser is null, attempting to log in...");
				Map<String, String> map;
				map = loginService.parseLoginBody(req.getReader());
				
				log.info("POST request body parsed");
				log.info("username provided: " + map.get("username"));
				//log.info("password provided: " + map.get("password"));
				log.info("Invoking LoginService");
				
				User user = loginService.login(map.get("username"), map.get("password"));
				if (user != null) {
					log.info("Creating new session");
					session = req.getSession();
					PrintWriter writer = resp.getWriter();
					
					session.setAttribute("currentUser", user);
					writer.write(new ObjectMapper().writeValueAsString(user));
					resp.setStatus(200);
					
					log.info(map.get("username") + " successfully logged in");
				} else {
					throw new LoginException("Unable to retrieve user. Username or password may be incorrect.");
				}
				
			} else {
				log.info("A request to login was received, but a session already exists.");
				throw new LoginException("Session already exists");
			}
				
		// Additional path
		} else if (portions.size() == 1) {
			String method = req.getMethod();
			
			// login/check
			if (portions.get(0).equals("check")) {
				session = req.getSession(false);
				
				if (!method.equals("GET")) {
					throw new InvalidMethodException("/login/check/ only accepts GET requests. Another request type was received.");
				}
				
				if (session == null || session.getAttribute("currentUser") == null) {
					throw new LoginException("User is not authorized and will need to login");
				}
				
				PrintWriter writer;
				try {
					writer = resp.getWriter();
			
					writer.write(new ObjectMapper().writeValueAsString(session.getAttribute("currentUser")));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				
				log.info("Session is active for " + session.getAttribute("currentUser"));
			} else {
				throw new InvalidURIPatternException("Invalid URI pattern sent to LoginController");
			}
			
		} else {
			throw new InvalidURIPatternException("Invalid URI pattern sent to LoginController");
		}
	}
	
}
