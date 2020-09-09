package com.revature.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.revature.dao.ReimbursementDAO;
import com.revature.exceptions.ResourceNotAvailableException;
import com.revature.utilities.ConnectionUtility;

public class DownloadService {

	private ReimbursementDAO reimbDao;
	
	private Logger log = Logger.getLogger(DownloadService.class);
	
	public DownloadService() {
		super();
		this.reimbDao = new ReimbursementDAO(ConnectionUtility.getConnection());
	}
	
	public DownloadService(ReimbursementDAO reimbDao) {
		this.reimbDao = reimbDao;
	}
	
	// We need to get the byte array, turn into an InputStream, send over to DownloadController,
	// resp.getOutputStream(), and write
	
	public InputStream getReceipt(int id) throws SQLException, ResourceNotAvailableException {
		byte[] receipt = this.reimbDao.getReimbReceipt(id);
		
		if (receipt == null) {
			throw new ResourceNotAvailableException("Receipt not found for reimbursement ID " + id);
		}
		
		InputStream receiptIs = new ByteArrayInputStream(receipt);
		
		return receiptIs;
	}
	
}
