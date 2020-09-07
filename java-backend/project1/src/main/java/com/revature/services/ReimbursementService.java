package com.revature.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;
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
	
	public List<Reimbursement> getAllReimbursementsByUserId(int id) throws SQLException {
		List<Reimbursement> reimbursements = reimbDao.getAllReimbursementsByUserId(id);
		
		return reimbursements;
	}
	
	public Reimbursement getReimbursementById(int id) throws SQLException {
		Reimbursement reimbursement = reimbDao.getReimbursementById(id);
		
		return reimbursement;
	}
	
	public boolean approveReimbursementById(int id, int resolverId) throws SQLException {
		if (reimbDao.approveReimbursementById(id, resolverId)) {
			return true;
		}
		
		return false;
	}
	
	public boolean denyReimbursementById(int id, int resolverId) throws SQLException {
		if (reimbDao.denyReimbursementById(id, resolverId)) {
			return true;
		}
		
		return false;
	}
	
	public boolean addReimbursement(Double amount, String description, int typeId, byte[] fileByte, int authorId) throws SQLException {
		if (reimbDao.addReimbursement(amount, description, typeId, fileByte, authorId)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String partToString(Part part) throws IOException {
		InputStream inputStream = part.getInputStream();
		
	    StringBuilder textBuilder = new StringBuilder();
	    try (Reader reader = new BufferedReader(new InputStreamReader
	      (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
	        int c = 0;
	        while ((c = reader.read()) != -1) {
	            textBuilder.append((char) c);
	        }
	    }
	    
	    return textBuilder.toString();
	}
	
	public byte[] partToByteArray(Part part) throws IOException {
		InputStream is = part.getInputStream();
		
		byte[] array = IOUtils.toByteArray(is);
		return array;
	}
	
}
