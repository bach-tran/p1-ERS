package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.revature.models.Reimbursement;
import com.revature.models.Role;
import com.revature.models.Status;
import com.revature.models.Type;
import com.revature.models.User;
import com.revature.utilities.ConnectionUtility;

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
				"(SELECT r.id as reimb_id, r.*, status.*, type.*, author.*, author.role_id as au_role, resolver.*, resolver.role_id as re_role " +
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
			// receipt is 7
			//int reimbAuthorId = rs.getInt(8); 
			int reimbResolverId = rs.getInt(9); // for logic, resolver can be null
			// reimbStatusId 10
			// reimbTypeId 11
			int statusId = rs.getInt(12);
			String status = rs.getString(13);
			Status statusObj = new Status(statusId, status);
			
			int typeId = rs.getInt(14);
			String type = rs.getString(15);
			Type typeObj = new Type(typeId, type);
			
			// ROLES
			int authorRoleId = rs.getInt(32);
			String authorRole = rs.getString(33);
			Role authorRoleObj = new Role(authorRoleId, authorRole);
			
			int resolverRoleId = rs.getInt(34);
			String resolverRole = rs.getString(35);
			Role resolverRoleObj = new Role(resolverRoleId, resolverRole);
			
			// Author
			int authorId = rs.getInt(16);
			String authorUsername = rs.getString(17);
			String authorPassword = rs.getString(18);
			String authorFirstname = rs.getString(19);
			String authorLastname = rs.getString(20);
			String authorEmail = rs.getString(21);
			User author = new User(authorId, authorUsername, authorPassword, authorFirstname, authorLastname, authorEmail, authorRoleObj);
			
			// author role_id 22 <- same
			// au_role 23 <- same
			
			// Resolver
			int resolverId = rs.getInt(24);
			String resolverUsername = rs.getString(25);
			String resolverPassword = rs.getString(26);
			String resolverFirstname = rs.getString(27);
			String resolverLastname = rs.getString(28);
			String resolverEmail = rs.getString(29);
			User resolver;
			if (resolverId == 0) {
				resolver = null;
			} else {
				resolver = new User(resolverId, resolverUsername, resolverPassword, resolverFirstname, resolverLastname, resolverEmail, resolverRoleObj);
			}
			// resolver role_id 30 <- same
			// re_role 31 <- same
			
			Reimbursement newReimb = new Reimbursement(reimbId, reimbAmount, reimbSubmitted, reimbResolved, reimbDescription, null, author, resolver, statusObj, typeObj);
			reimbursements.add(newReimb);
		}
		
		return reimbursements;
	}
	
	public static void main(String[] args) throws SQLException {
		ReimbursementDAO dao = new ReimbursementDAO(ConnectionUtility.getConnection());
		
		List<Reimbursement> list = dao.getAllReimbursements();
		
		for (Reimbursement reimb : list) {
			if (reimb.getResolver() != null) {
				reimb.getResolver().setPassword(null);
			}
			System.out.println(reimb);
		}
	}

}
