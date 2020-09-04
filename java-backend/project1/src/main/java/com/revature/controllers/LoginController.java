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

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException, UnexpectedRequestBodyException {
		log.info("Processing request");
		session = req.getSession(false);
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			if (!method.equals("POST")) {
				throw new InvalidMethodException("LoginController only accepts POST requests. Another request type was received.");
			}
			
			try {
				if (session == null) {
					Map<String, String> map;
					map = parsePostBody(req);
					
					log.info("POST request body parsed");
					log.info("username provided: " + map.get("username"));
					log.info("password provided: " + map.get("password"));
					log.info("Invoking LoginService");
					
					User user = loginService.login(map.get("username"), map.get("password"));
					if (user != null) {
						session = req.getSession();
						PrintWriter writer = resp.getWriter();
						log.info(map.get("username") + " successfully logged in");
						
						session.setAttribute("currentUser", user);
						writer.write(new ObjectMapper().writeValueAsString(user));
						resp.setStatus(200);
					} else {
						throw new LoginException("Unable to retrieve user. Username or password may be incorrect.");
					}
					
				} else {
					log.info("A request to login was received, but a session already exists.");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else if (portions.size() == 1) {
			if (portions.get(0).equals("check")) {
				if (session == null || session.getAttribute("currentUser") == null) {
					throw new LoginException("User is not authorized and will need to login");
				}
				
				PrintWriter writer;
				try {
					writer = resp.getWriter();
			
					writer.write(new ObjectMapper().writeValueAsString(session.getAttribute("currentUser")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
	
	public Map<String, String> parsePostBody(HttpServletRequest req) throws IOException, UnexpectedRequestBodyException {
		BufferedReader reader = req.getReader();
		
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
	
}
