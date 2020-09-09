package com.revature.services;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.revature.dao.ReimbursementDAO;
import com.revature.exceptions.ResourceNotAvailableException;

public class DownloadServiceTest {
	
	private DownloadService downloadService;
	private ReimbursementDAO reimbDao;

	@Before
	public void setUp() throws Exception {
		reimbDao = mock(ReimbursementDAO.class);
		downloadService = new DownloadService(reimbDao);
	}

	@Test(expected=ResourceNotAvailableException.class)
	public void testReceiptNull() throws SQLException, ResourceNotAvailableException {
		when(reimbDao.getReimbReceipt(30)).thenReturn(null);
		
		downloadService.getReceipt(30);
	}
	
	@Test
	public void testReceiptNotNull() throws SQLException, ResourceNotAvailableException {
		when(reimbDao.getReimbReceipt(1)).thenReturn("test".getBytes());
		
		downloadService.getReceipt(1);
	}

}
