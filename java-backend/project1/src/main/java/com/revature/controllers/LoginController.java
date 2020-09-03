package com.revature.controllers;

import java.io.BufferedReader;
import java.io.IOException;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.ReadRequestException;
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

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, InvalidURIPatternException, ReadRequestException, LoginException {
		log.info("Processing request");
		session = req.getSession(false);
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			if (!method.equals("POST")) {
				throw new InvalidMethodException("LoginController only accepts POST requests. Another request type was received.");
			}
			
			try {
				if (session == null) {
					Map<String, String> map = parsePostBody(req);
					log.info("POST request body parsed");
					log.info("username provided: " + map.get("username"));
					log.info("password provided: " + map.get("password"));
					log.info("Invoking LoginService");
					if (loginService.login(map.get("username"), map.get("password"))) {
						log.info(map.get("username") + " successfully logged in");
						session = req.getSession();
						session.setAttribute("username", map.get("username"));
					} else {
						throw new LoginException("Error Attempting to log in!");
					}
				} else {
					log.info(session.getAttribute("username") + " is already logged in");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			throw new InvalidURIPatternException("LoginController accepts 0 additional URI sections");
		}
	}
	
	public Map<String, String> parsePostBody(HttpServletRequest req) throws IOException {
		BufferedReader reader = req.getReader();
		
		String body = reader.lines().collect(Collectors.joining());
		
		ObjectMapper om = new ObjectMapper();
		ObjectNode node = om.readValue(body, ObjectNode.class);
		
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
