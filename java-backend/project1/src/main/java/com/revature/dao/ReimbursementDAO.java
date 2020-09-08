package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;

public class ReimbursementDAO {

	private Connection con;
	private UserDAO userDao;
		
	public ReimbursementDAO(Connection con) {
		super();
		this.con = con;
		this.userDao = new UserDAO(this.con);
	}
	
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	public List<Reimbursement> getAllReimbursements() throws SQLException {
		String sql = "SELECT * " +
				"FROM " +
				"(SELECT r.id as reimb_id, r.id, r.amount, r.submitted, r.resolved, r.description, r.author, r.resolver, status.*, type.*, author.*, author.role_id as au_role, resolver.*, resolver.role_id as re_role " +
				"FROM reimbursements r " +
				"INNER JOIN r_status status " +
				"ON r.status_id = status.id " +
				"INNER JOIN r_type type " +
				"ON r.type_id = type.id " +
				"INNER JOIN users author " +
				"ON r.author = author.id " +
				"LEFT JOIN users resolver " +
				"ON r.resolver = resolver.id) au_re " +
				"LEFT JOIN user_roles ur " +
				"ON au_re.au_role = ur.id " +
				"LEFT JOIN user_roles ur2 " +
				"ON au_re.re_role = ur2.id";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		
		List<Reimbursement> reimbursements = new ArrayList<>();
		
		while (rs.next()) {
			// Reimbursement columns
			int reimbId = rs.getInt(1);
			double reimbAmount = rs.getDouble(3);
			Timestamp reimbSubmitted = rs.getTimestamp(4);
			Timestamp reimbResolved = rs.getTimestamp(5);
			String reimbDescription = rs.getString(6);
			//int reimbAuthorId = rs.getInt(7); 
			int reimbResolverId = rs.getInt(8); // for logic, resolver can be null
			// reimbStatusId 10
			// reimbTypeId 11
			int statusId = rs.getInt(9);
			String status = rs.getString(10);
			Status statusObj = new Status(statusId, status);
			
			int typeId = rs.getInt(11);
			String type = rs.getString(12);
			Type typeObj = new Type(typeId, type);
			
			// ROLES
			int authorRoleId = rs.getInt(29);
			String authorRole = rs.getString(30);
			Role authorRoleObj = new Role(authorRoleId, authorRole);
			
			int resolverRoleId = rs.getInt(31);
			String resolverRole = rs.getString(32);
			Role resolverRoleObj = new Role(resolverRoleId, resolverRole);
			
			// Author
			int authorId = rs.getInt(13);
			String authorUsername = rs.getString(14);
			String authorPassword = rs.getString(15);
			String authorFirstname = rs.getString(16);
			String authorLastname = rs.getString(17);
			String authorEmail = rs.getString(18);
			User author = new User(authorId, authorUsername, authorPassword, authorFirstname, authorLastname, authorEmail, authorRoleObj);
			
			// author role_id 19 <- same
			// au_role 20 <- same
			
			// Resolver
			int resolverId = rs.getInt(21);
			String resolverUsername = rs.getString(22);
			String resolverPassword = rs.getString(23);
			String resolverFirstname = rs.getString(24);
			String resolverLastname = rs.getString(25);
			String resolverEmail = rs.getString(26);
			User resolver;
			if (resolverId == 0) {
				resolver = null;
			} else {
				resolver = new User(resolverId, resolverUsername, resolverPassword, resolverFirstname, resolverLastname, resolverEmail, resolverRoleObj);
			}
			// resolver role_id 27 <- same
			// re_role 28 <- same
			
			Reimbursement newReimb = new Reimbursement(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, null, author, resolver, statusObj, typeObj);
			reimbursements.add(newReimb);
		}
		
		return reimbursements;
	}
	
	public List<Reimbursement> getAllReimbursementsByUserId(int id) throws SQLException {
		String sql = "SELECT * " +
				"FROM " +
				"(SELECT r.id as reimb_id, r.id, r.amount, r.submitted, r.resolved, r.description, r.author, r.resolver, status.*, type.*, author.*, author.role_id as au_role, resolver.*, resolver.role_id as re_role " +
				"FROM reimbursements r " +
				"INNER JOIN r_status status " +
				"ON r.status_id = status.id " +
				"INNER JOIN r_type type " +
				"ON r.type_id = type.id " +
				"INNER JOIN users author " +
				"ON r.author = author.id " +
				"LEFT JOIN users resolver " +
				"ON r.resolver = resolver.id) au_re " +
				"LEFT JOIN user_roles ur " +
				"ON au_re.au_role = ur.id " +
				"LEFT JOIN user_roles ur2 " +
				"ON au_re.re_role = ur2.id "
				+ "WHERE author = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		List<Reimbursement> reimbursements = new ArrayList<>();
		
		while (rs.next()) {
			// Reimbursement columns
			int reimbId = rs.getInt(1);
			double reimbAmount = rs.getDouble(3);
			Timestamp reimbSubmitted = rs.getTimestamp(4);
			Timestamp reimbResolved = rs.getTimestamp(5);
			String reimbDescription = rs.getString(6);
			//int reimbAuthorId = rs.getInt(7); 
			int reimbResolverId = rs.getInt(8); // for logic, resolver can be null
			// reimbStatusId 10
			// reimbTypeId 11
			int statusId = rs.getInt(9);
			String status = rs.getString(10);
			Status statusObj = new Status(statusId, status);
			
			int typeId = rs.getInt(11);
			String type = rs.getString(12);
			Type typeObj = new Type(typeId, type);
			
			// ROLES
			int authorRoleId = rs.getInt(29);
			String authorRole = rs.getString(30);
			Role authorRoleObj = new Role(authorRoleId, authorRole);
			
			int resolverRoleId = rs.getInt(31);
			String resolverRole = rs.getString(32);
			Role resolverRoleObj = new Role(resolverRoleId, resolverRole);
			
			// Author
			int authorId = rs.getInt(13);
			String authorUsername = rs.getString(14);
			String authorPassword = rs.getString(15);
			String authorFirstname = rs.getString(16);
			String authorLastname = rs.getString(17);
			String authorEmail = rs.getString(18);
			User author = new User(authorId, authorUsername, authorPassword, authorFirstname, authorLastname, authorEmail, authorRoleObj);
			
			// author role_id 19 <- same
			// au_role 20 <- same
			
			// Resolver
			int resolverId = rs.getInt(21);
			String resolverUsername = rs.getString(22);
			String resolverPassword = rs.getString(23);
			String resolverFirstname = rs.getString(24);
			String resolverLastname = rs.getString(25);
			String resolverEmail = rs.getString(26);
			User resolver;
			if (resolverId == 0) {
				resolver = null;
			} else {
				resolver = new User(resolverId, resolverUsername, resolverPassword, resolverFirstname, resolverLastname, resolverEmail, resolverRoleObj);
			}
			// resolver role_id 27 <- same
			// re_role 28 <- same
			
			Reimbursement newReimb = new Reimbursement(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, null, author, resolver, statusObj, typeObj);
			reimbursements.add(newReimb);
		}
		
		return reimbursements;
	}
	
	public Reimbursement getReimbursementById(int id) throws SQLException {
		String sql = "SELECT * " +
				"FROM " +
				"(SELECT r.id as reimb_id, r.id, r.amount, r.submitted, r.resolved, r.description, r.author, r.resolver, status.*, type.*, author.*, author.role_id as au_role, resolver.*, resolver.role_id as re_role " +
				"FROM reimbursements r " +
				"INNER JOIN r_status status " +
				"ON r.status_id = status.id " +
				"INNER JOIN r_type type " +
				"ON r.type_id = type.id " +
				"INNER JOIN users author " +
				"ON r.author = author.id " +
				"LEFT JOIN users resolver " +
				"ON r.resolver = resolver.id) au_re " +
				"LEFT JOIN user_roles ur " +
				"ON au_re.au_role = ur.id " +
				"LEFT JOIN user_roles ur2 " +
				"ON au_re.re_role = ur2.id "
				+ "WHERE reimb_id = ?";
		
		PreparedStatement stmt = con.prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		ResultSet rs = stmt.executeQuery();
		
		while (rs.next()) {
			// Reimbursement columns
			int reimbId = rs.getInt(1);
			double reimbAmount = rs.getDouble(3);
			Timestamp reimbSubmitted = rs.getTimestamp(4);
			Timestamp reimbResolved = rs.getTimestamp(5);
			String reimbDescription = rs.getString(6);
			//int reimbAuthorId = rs.getInt(7); 
			int reimbResolverId = rs.getInt(8); // for logic, resolver can be null
			// reimbStatusId 10
			// reimbTypeId 11
			int statusId = rs.getInt(9);
			String status = rs.getString(10);
			Status statusObj = new Status(statusId, status);
			
			int typeId = rs.getInt(11);
			String type = rs.getString(12);
			Type typeObj = new Type(typeId, type);
			
			// ROLES
			int authorRoleId = rs.getInt(29);
			String authorRole = rs.getString(30);
			Role authorRoleObj = new Role(authorRoleId, authorRole);
			
			int resolverRoleId = rs.getInt(31);
			String resolverRole = rs.getString(32);
			Role resolverRoleObj = new Role(resolverRoleId, resolverRole);
			
			// Author
			int authorId = rs.getInt(13);
			String authorUsername = rs.getString(14);
			String authorPassword = rs.getString(15);
			String authorFirstname = rs.getString(16);
			String authorLastname = rs.getString(17);
			String authorEmail = rs.getString(18);
			User author = new User(authorId, authorUsername, authorPassword, authorFirstname, authorLastname, authorEmail, authorRoleObj);
			
			// author role_id 19 <- same
			// au_role 20 <- same
			
			// Resolver
			int resolverId = rs.getInt(21);
			String resolverUsername = rs.getString(22);
			String resolverPassword = rs.getString(23);
			String resolverFirstname = rs.getString(24);
			String resolverLastname = rs.getString(25);
			String resolverEmail = rs.getString(26);
			User resolver;
			if (resolverId == 0) {
				resolver = null;
			} else {
				resolver = new User(resolverId, resolverUsername, resolverPassword, resolverFirstname, resolverLastname, resolverEmail, resolverRoleObj);
			}
			// resolver role_id 27 <- same
			// re_role 28 <- same
			
			Reimbursement newReimb = new Reimbursement(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, null, author, resolver, statusObj, typeObj);
			
			return newReimb;
		}
		
		return null;
	}
	

	public boolean addReimbursement(Double amount, String description, int typeId, byte[] fileByte, int authorId) throws SQLException {
		String sql = "INSERT INTO reimbursements "
				+ "(amount, submitted, description, receipt, author, status_id, type_id) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?)";
		
		PreparedStatement stmt = this.con.prepareStatement(sql);
		
		stmt.setDouble(1, amount);
		stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
		stmt.setString(3, description);
		stmt.setBytes(4, fileByte);
		stmt.setInt(5, authorId);
		stmt.setInt(6, 1);
		stmt.setInt(7, typeId);
		
		
		int count = stmt.executeUpdate();
		
		if (count == 1) {
			return true;
		}
		
		return false;
	}
	
	public boolean approveReimbursementById(int id, int resolverId) throws SQLException {
		String sql = "UPDATE reimbursements "
				+ "SET resolved = ?, "
				+ "resolver = ?, "
				+ "status_id = ? "
				+ "WHERE id = ?";
		
		PreparedStatement stmt = this.con.prepareStatement(sql);
		
		stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		stmt.setInt(2, resolverId);
		stmt.setInt(3, 2);
		stmt.setInt(4, id);
		
		int count = stmt.executeUpdate();
		
		if (count == 1) {
			con.commit();
			return true;
		}
		
		return false;
	}
	
	public boolean denyReimbursementById(int id, int resolverId) throws SQLException {
		String sql = "UPDATE reimbursements "
				+ "SET resolved = ?, "
				+ "resolver = ?, "
				+ "status_id = ? "
				+ "WHERE id = ?";
		
		PreparedStatement stmt = this.con.prepareStatement(sql);
		
		stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
		stmt.setInt(2, resolverId);
		stmt.setInt(3, 3);
		stmt.setInt(4, id);
		
		int count = stmt.executeUpdate();
		
		if (count == 1) {
			con.commit();
			return true;
		}
		
		return false;
	}
	
//	public static void main(String[] args) throws SQLException {
//		ReimbursementDAO dao = new ReimbursementDAO(ConnectionUtility.getConnection());
//		
//		List<Reimbursement> list = dao.getAllReimbursements();
//		
//		for (Reimbursement reimb : list) {
//			if (reimb.getResolver() != null) {
//				reimb.getResolver().setPassword(null);
//			}
//			System.out.println(reimb);
//		}
//	}
	
}
