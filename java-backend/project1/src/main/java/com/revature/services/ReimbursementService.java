package com.revature.services;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.dao.ReimbursementDAO;
import com.revature.dao.UserDAO;
import com.revature.models.Reimbursement;
import com.revature.utilities.ConnectionUtility;

public class ReimbursementService {

	private ReimbursementDAO reimbDao;
	private UserDAO userDao;
	
	private Logger log = Logger.getLogger(ReimbursementService.class);
	
	public ReimbursementService() {
		super();
		reimbDao = new ReimbursementDAO(ConnectionUtility.getConnection());
		userDao = new UserDAO(ConnectionUtility.getConnection());
	}
	
	public ReimbursementService(ReimbursementDAO reimbDao, UserDAO userDao) {
		this.reimbDao = reimbDao;
		this.userDao = userDao;
	}

	public List<Reimbursement> getAllReimbursements() throws SQLException {
		List<Reimbursement> reimbursements = reimbDao.getAllReimbursements();
		
		return reimbursements;
	}
	
}
