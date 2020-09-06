package com.revature.services;

import org.apache.log4j.Logger;

import com.revature.dao.ReimbursementDAO;
import com.revature.utilities.ConnectionUtility;

public class ReimbursementService {

	private ReimbursementDAO dao;
	private Logger log = Logger.getLogger(ReimbursementService.class);
	
	public ReimbursementService() {
		super();
		dao = new ReimbursementDAO(ConnectionUtility.getConnection());
	}
	
}
