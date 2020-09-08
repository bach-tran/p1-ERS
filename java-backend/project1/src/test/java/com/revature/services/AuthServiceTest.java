package com.revature.services;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.revature.exceptions.NotAuthorizedException;
import com.revature.models.Role;
import com.revature.models.User;

public class AuthServiceTest {
	
	private HttpSession session;

	@Before
	public void setUp() {
		session = mock(HttpSession.class);
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testSessionNull() throws NotAuthorizedException {
		AuthService service = new AuthService();
		Role[] roles = new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")};
		
		session = null;
		
		service.guard(session, roles);
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testSessionCurrentUserNull() throws NotAuthorizedException {
		AuthService service = new AuthService();
		Role[] roles = new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")};
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(null);
		
		service.guard(session, roles);
	}
	
	@Test
	public void testSessionSuccessfullyAuthenticated() throws NotAuthorizedException {
		AuthService service = new AuthService();
		Role[] roles = new Role[] {new Role(1, "EMPLOYEE"), new Role(2, "MANAGER")};
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5",
				"Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		service.guard(session, roles);
	}
	
	@Test(expected=NotAuthorizedException.class)
	public void testSessionUnsuccessfullyAuthenticated() throws NotAuthorizedException {
		AuthService service = new AuthService();
		Role[] roles = new Role[] {new Role(2, "MANAGER")};
		
		when(session.getAttribute(eq("currentUser"))).thenReturn(new User(1, "bach_tran", "5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5",
				"Bach", "Tran", "bach_tran@outlook.com", new Role(1, "EMPLOYEE")));
		
		service.guard(session, roles);
	}
}
