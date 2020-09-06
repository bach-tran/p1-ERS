package com.revature.dao;

import java.sql.Connection;

public class ReimbursementDAO {

	private Connection con;
	
	public ReimbursementDAO() {
		super();
	}
	
	public ReimbursementDAO(Connection con) {
		super();
		this.con = con;
	}
	
	public void setConnection(Connection con) {
		this.con = con;
	}

}
