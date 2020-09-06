package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.models.Role;
import com.revature.models.User;

public class UserDAO {

	private Connection con;
	
	public UserDAO() {
		super();
	}
	
	public UserDAO(Connection con) {
		super();
		this.con = con;
	}
	
	// Connection Setter
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	public User getUserByUsername(String user) throws SQLException {
		String sql = "SELECT u.id, u.username, u.password, u.first_name, u.last_name, u.email, ur.id, ur.role " +
				"FROM users u "
				+ "INNER JOIN user_roles ur "
				+ "ON u.role_id = ur.id "
				+ "WHERE u.username = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setString(1, user);
		
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			int id = rs.getInt(1);
			String username = rs.getString(2);
			String password = rs.getString(3);
			String firstName = rs.getString(4);
			String lastName = rs.getString(5);
			String email = rs.getString(6);
			int roleId = rs.getInt(7);
			String role = rs.getString(8);
			
			User newUser = new User(id, username, password, firstName, lastName, email, new Role(roleId, role));
			
			return newUser;
		}
		
		return null;
	}
	
	public User getUserById(int id) throws SQLException {
		String sql = "SELECT u.id, u.username, u.password, u.first_name, u.last_name, u.email, ur.id, ur.role " +
				"FROM users u " +
				"INNER JOIN user_roles ur " +
				"ON u.role_id = ur.id " +
				"WHERE u.id = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			int user_id = rs.getInt(1);
			String username = rs.getString(2);
			String password = rs.getString(3);
			String firstName = rs.getString(4);
			String lastName = rs.getString(5);
			String email = rs.getString(6);
			int roleId = rs.getInt(7);
			String role = rs.getString(8);
			
			User newUser = new User(user_id, username, password, firstName, lastName, email, new Role(roleId, role));
			
			return newUser;
		}
		
		return null;
	}
	
	public boolean createUser(User user) {
		return false;
	}
	
	public boolean updateUser(User user) {
		return false;
	}
	
	public boolean deleteUser(User user) {
		return false;
	}

}
