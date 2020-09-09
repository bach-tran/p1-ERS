package com.revature.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.DownloadService;
import com.revature.services.ReimbursementService;

public class DownloadController {

	private Logger log = Logger.getLogger(DownloadController.class);
	
	private HttpSession session;
	private DownloadService downloadService;
	private AuthService authService;
	private ReimbursementService reimbService;
	private ObjectMapper mapper;

	
	public DownloadController() {
		super();
		this.downloadService = new DownloadService();
		this.authService = new AuthService();
		this.reimbService = new ReimbursementService();
		this.mapper = new ObjectMapper();
	}
	
	public DownloadController(DownloadService downloadService, AuthService authService, ReimbursementService reimbService, ObjectMapper mapper) {
		this.downloadService = downloadService;
		this.authService = authService;
		this.reimbService = reimbService;
		this.mapper = mapper;
	}
	
	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidURIPatternException, InvalidMethodException, NotAuthorizedException, SQLException, ResourceNotAvailableException, IOException {
		log.info("Processing request");
		
		if (portions.size() == 2) {
			if (portions.get(0).equals("reimb")) {
				
				String method = req.getMethod();
				
				// Get ID portion and check if the ID portion is parseable to int
				int id;
				try {
					id = Integer.parseInt(portions.get(1));
				} catch (NumberFormatException e) {
					throw new InvalidURIPatternException("An ID was expected for /download/reimb/<>");
				}
				
				if (method.equals("GET")) {
					log.info("Attempting to GET download");
					session = req.getSession(false);
					this.authService.guard(session, new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")});
					
					User currentUser = (User) session.getAttribute("currentUser");
					Role currentRole = currentUser.getRole();
					
					if (currentRole.getRole().equals("EMPLOYEE") && 
							!(currentUser.getId() == this.reimbService.getReimbursementById(id).getAuthor().getId())) {
						throw new NotAuthorizedException("Employees can only download their own reimbursements");
					}
					
					
					InputStream receipt = this.downloadService.getReceipt(id);
					String mimeType = URLConnection.guessContentTypeFromStream(receipt);
					log.info("Retrieved file from database");
					
					resp.setContentType(mimeType);
					
					// Write file to response
					OutputStream os = resp.getOutputStream();
					byte[] buffer = new byte[1000];
					int chunk;
					while ((chunk = receipt.read(buffer)) != -1) {
						os.write(buffer, 0, chunk);
					}
					
					log.info("File written to response");
					
					resp.setStatus(200);
					
				} else {
					throw new InvalidMethodException("Invalid method provided for /download/reimb/<>");
				}
				
			} else {
				throw new InvalidURIPatternException("Invalid URI pattern sent to DownloadController");
			}
		} else {
			throw new InvalidURIPatternException("Invalid URI pattern sent to DownloadController");
		}
	}
	

}
