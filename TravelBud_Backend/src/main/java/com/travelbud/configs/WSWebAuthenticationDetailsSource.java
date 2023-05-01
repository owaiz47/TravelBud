package com.travelbud.configs;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.travelbud.entities.User;

public class WSWebAuthenticationDetailsSource extends WebAuthenticationDetails{
	private static final long serialVersionUID = 1L;
	private User authenticatedUser;
	
	public WSWebAuthenticationDetailsSource(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}

	public User getAuthenticatedUser() {
		return authenticatedUser;
	}

	public void setAuthenticatedUser(User authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}
	
	

}
