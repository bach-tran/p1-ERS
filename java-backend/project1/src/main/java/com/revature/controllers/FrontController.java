package com.revature.controllers;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.revature.exceptions.InvalidMethodException;
import com.revature.exceptions.InvalidURIPatternException;
import com.revature.exceptions.LoginException;
import com.revature.exceptions.NotAllowedException;
import com.revature.exceptions.NotAuthorizedException;
import com.revature.exceptions.ReadRequestException;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.exceptions.UnexpectedRequestBodyException;
import com.revature.exceptions.UnexpectedRequestParametersException;

@MultipartConfig
@SuppressWarnings("serial")
public class FrontController extends HttpServlet {
	
	private Logger log = Logger.getLogger(FrontController.class);
	
	private ReimbursementController reimbursementController;
	private LoginController loginController;
	private LogoutController logoutController;
	private DownloadController downloadController;
	
	public FrontController() {
		super();
		loginController = new LoginController();
		logoutController = new LogoutController();
		reimbursementController = new ReimbursementController();
		downloadController = new DownloadController();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		final String URI = req.getRequestURI().replace("/project1", "");
		log.info("Request URI: " + URI);
		
		// Check for invalid start of URI
		if (URI.charAt(0) != ('/')) {
			resp.setStatus(400);
			return;
		}
		
		// Split the URI into the resource being accessed and the portions afterwards
		String[] splitURI = URI.substring(1).split("/");
		final String resource = splitURI[0];
		
		List<String> portions = new ArrayList<String>();
		for (int i = 0; i < splitURI.length - 1; i++) {
			if (splitURI[i+1].equals("")) {
				resp.setStatus(400);
				return;
			}
			portions.add(splitURI[i+1]);
		}
		
		try {
			switch (resource) {
			case "login":
				log.info("LoginController invoked");
				loginController.process(req, resp, portions);
				break;
			case "logout":
				log.info("LogoutController invoked");
				logoutController.process(req, resp, portions);
				break;
			case "reimb":
				log.info("ReimbursementController invoked");
				reimbursementController.process(req, resp, portions);
				break;
			case "download":
				log.info("DownloadController invoked");
				downloadController.process(req, resp, portions);
				break;
			default:
				resp.setStatus(400);
				return;
			}
		} catch (InvalidMethodException | InvalidURIPatternException | UnexpectedRequestBodyException | UnexpectedRequestParametersException e) {
			log.error(e.getMessage());
			resp.setStatus(400);
			return;
		} catch (LoginException | NotAuthorizedException e) {
			log.error(e.getMessage());
			resp.setStatus(401);
		} catch (ReadRequestException | NoSuchAlgorithmException | SQLException e) {
			log.error(e.getMessage());
			resp.setStatus(500);
		} catch (ResourceNotAvailableException | NotAllowedException e) {
			log.error(e.getMessage());
			resp.setStatus(404);
		} 
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
}
