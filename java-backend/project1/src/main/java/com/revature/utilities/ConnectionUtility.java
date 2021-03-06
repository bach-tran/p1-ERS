package com.revature.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.Driver;

public class ConnectionUtility {
	
	public static final String URL = "jdbc:postgresql://training-ohio.clwebyd8kmnl.us-east-2.rds.amazonaws.com/T200810?currentSchema=my_project1";
	public static final String USER = "root";
	public static final String PASS = "password";
	
	private ConnectionUtility() {
		super();
	}
	
	public static Connection getConnection() {		
		try {
			DriverManager.registerDriver(new Driver());
			return DriverManager.getConnection(URL, USER, PASS);
		} catch (SQLException e) {
			throw new RuntimeException("Error getting connection from the database", e);
		}
	}
	
}
