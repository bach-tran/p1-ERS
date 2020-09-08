package com.revature.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.NotAllowedException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.exceptions.UnexpectedRequestParametersException;
import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.User;
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

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException, NotAuthorizedException, SQLException, IOException, ServletException, UnexpectedRequestBodyException, InvalidURIPatternException, ResourceNotAvailableException, NotAllowedException, UnexpectedRequestParametersException {
		log.info("Processing request");
		
		if (portions.size() == 0) {
			String method = req.getMethod();
			
			// Get ALL reimbursements
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
			
			// Add reimbursement
			} else if (method.equals("POST")) {
				
				log.info("Attempting to add reimbursement");
				session = req.getSession(false);
				
				this.authService.guard(session, new Role[] {new Role(1, "EMPLOYEE")});
				
				Part partAmount = req.getPart("amount");
				Part partDescription = req.getPart("description");
				Part partTypeId = req.getPart("type");
				Part partFile = req.getPart("file");
				
				String description = this.reimbService.partToString(partDescription);
				Double amount;
				int typeId;
				try {
					amount = Double.parseDouble(this.reimbService.partToString(partAmount).trim());
					typeId = Integer.parseInt(this.reimbService.partToString(partTypeId).trim());
				} catch (NumberFormatException e) {
					throw new UnexpectedRequestBodyException("Reimbursement request data contains invalid data for either amount or typeId");
				}
				
				if (partFile != null) {
					byte[] fileByte = this.reimbService.partToByteArray(partFile);
					String[] fileInfo = partFile.getSubmittedFileName().split("\\.");
					
					String fileName = fileInfo[0];
					String extension = fileInfo[1];
					
					if (!(extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png") || extension.equals("gif"))) {
						throw new UnexpectedRequestBodyException("Only jpg, png, and gif files can be uploaded");
					}
					
					User currentUser = (User) session.getAttribute("currentUser");
					
					this.reimbService.addReimbursement(amount, description, typeId, fileByte, currentUser.getId());
					
					log.info("Reimbursement successfully added");
					resp.setStatus(200);
					
				} else {
					throw new UnexpectedRequestBodyException("Reimbursement requires an image to be uploaded");
				}
				
				
			} else {
				throw new InvalidMethodException("/reimb/ does not support " + req.getMethod());
			}
			
		} else if (portions.size() == 1) {
			
			String method = req.getMethod();
			
			int id;
			try {
				id = Integer.parseInt(portions.get(0));
			} catch (NumberFormatException e) {
				throw new InvalidURIPatternException("An ID was expected for /reimb/<>");
			}
			
			// Get reimbursement by id
			if (method.equals("GET")) {
				session = req.getSession(false);
				this.authService.guard(session, new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")});
				
				Reimbursement reimbursement = this.reimbService.getReimbursementById(id);
				if (reimbursement == null) {
					throw new ResourceNotAvailableException("The requested resource is not available");
				}
				
				User currentUser = (User) session.getAttribute("currentUser");
				// if current user is not the author AND the current user is an EMPLOYEE, throw exception
				if (!reimbursement.getAuthor().equals(currentUser) && currentUser.getRole().equals(new Role(1, "EMPLOYEE"))) {
					throw new NotAuthorizedException("Employees can only access their own reimbursement");
				}
				
				PrintWriter writer = resp.getWriter();
				
				writer.write(mapper.writeValueAsString(reimbursement));
				resp.setStatus(200);
				
				log.info("Reimbursement successfully written to response");
			} else if (method.equals("PUT")) {
				session = req.getSession(false);
				this.authService.guard(session, new Role[] {new Role(2, "MANAGER")});
				
				Reimbursement reimbursement = this.reimbService.getReimbursementById(id);
				
				if (!reimbursement.getStatus().equals(new Status(1, "PENDING"))) {
					throw new NotAllowedException("Cannot modify a reimbursement that is already approved or denied");
				}
				
				String operation = req.getParameter("operation");
				if (operation == null) {
					throw new UnexpectedRequestParametersException("Parameters provided are invalid");
				}
				
				User manager = (User) session.getAttribute("currentUser");
				int resolverId = manager.getId();
				String resolverName = manager.getFirstName() + " " + manager.getLastName();
				
				if (operation.equals("approve")) {
					this.reimbService.approveReimbursementById(id, resolverId);
					log.info("Reimbursement id " + id + " approved by " + resolverName);
				} else if (operation.equals("deny")) {
					this.reimbService.denyReimbursementById(id, resolverId);
					log.info("Reimbursement id " + id + " denied by " + resolverName);
				} else {
					throw new UnexpectedRequestParametersException("Value provided for operation parameter is invalid");
				}
				
			}
		} else if (portions.size() == 2) {
			if (portions.get(0).equals("user")) {
				
				String method = req.getMethod();
				
				int id;
				try {
					id = Integer.parseInt(portions.get(1));
				} catch (NumberFormatException e) {
					throw new InvalidURIPatternException("An ID was expected for /reimb/<>");
				}
				
				if (method.equals("GET")) {
					session = req.getSession(false);
					this.authService.guard(session, new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")});
					
					User currentUser = (User) session.getAttribute("currentUser");
					Role currentRole = currentUser.getRole();
					
					if (currentRole.equals(new Role(1, "EMPLOYEE")) && currentUser.getId() != id) {
						throw new NotAuthorizedException("Employee role is not allowed to access other employee reimbursements");
					}
					
					List<Reimbursement> reimbursements = this.reimbService.getAllReimbursementsByUserId(id);
					
					PrintWriter writer = resp.getWriter();
					
					mapper.enable(SerializationFeature.INDENT_OUTPUT);
					writer.write(mapper.writeValueAsString(reimbursements));
					
					resp.setStatus(200);
					
					log.info("Reimbursements successfully written to response");
					
				} else {
					throw new InvalidMethodException("Invalid method provided for /reimb/user/<id>");
				}
				
			} else {
				throw new InvalidURIPatternException("Invalid URI pattern sent to ReimbursementController");
			}
		} else {
			throw new InvalidURIPatternException("Invalid URI pattern sent to ReimbursementController");
		}
	}
	
}
