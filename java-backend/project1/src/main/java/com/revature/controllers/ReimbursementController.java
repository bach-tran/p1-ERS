package com.revature.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.services.AuthService;
import com.revature.services.ReimbursementService;

public class ReimbursementController {
	
	private Logger log = Logger.getLogger(ReimbursementController.class);
	
	private HttpSession session;
	
	private AuthService authService;
	private ReimbursementService reimbService;
	private ObjectMapper mapper;
	
	
	public ReimbursementController() {
		super();
		this.authService = new AuthService();
		this.reimbService = new ReimbursementService();
		this.mapper = new ObjectMapper();
	}
	
	public ReimbursementController(AuthService authService, ReimbursementService reimbService, ObjectMapper mapper) {
		this.authService = authService;
		this.reimbService = reimbService;
		this.mapper = mapper;
	}

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, NotAuthorizedException, SQLException, IOException {
		log.info("Processing request");
		
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			if (method.equals("GET")) { 
				log.info("Attempting to retrieve all reimbursements");
				session = req.getSession(false);
				
				this.authService.guard(session, new Role[] {new Role(2, "MANAGER")});
				
				List<Reimbursement> reimbursements = this.reimbService.getAllReimbursements();
				
				PrintWriter writer = resp.getWriter();
				
			    mapper.enable(SerializationFeature.INDENT_OUTPUT);
				writer.write(mapper.writeValueAsString(reimbursements));
				
				resp.setStatus(200);
				log.info("Reimbursements successfully written to response");
			} else {
				throw new InvalidMethodException("/reimb/ does not support " + req.getMethod());
			}
		}
	}
	
}
