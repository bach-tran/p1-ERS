package com.revature.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.revature.exceptions.InvalidMethodException;

public class ReimbursementController {
	
	private Logger log = Logger.getLogger(ReimbursementController.class);
	private HttpSession session;

	public void process(HttpServletRequest req, HttpServletResponse resp, List<String> portions) throws InvalidMethodException {
		log.info("Processing request");
		
		if (portions.size() == 0) {
			if (req.getMethod().equals("GET")) { 
				
			} else {
				throw new InvalidMethodException("/reimb/ does not support " + req.getMethod());
			}
		}
	}
	
}
