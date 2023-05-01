package com.travelbud.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.travelbud.configs.WSWebAuthenticationDetailsSource;
import com.travelbud.services.CustomUserDetailsService;
import com.travelbud.services.UserService;
import com.travelbud.utils.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private CustomUserDetailsService service;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = httpServletRequest.getHeader("Authorization");
		authorize(authorizationHeader, httpServletRequest);
		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	public void authorize(String authorizationHeader, HttpServletRequest httpServletRequest) {
		if(authorizationHeader == null)return;
		String token = null;
		String userName = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
			userName = jwtUtil.extractUsername(token);
		}

		if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			UserDetails userDetails = service.loadUserByUsername(userName);

			if (jwtUtil.validateToken(token, userDetails)) {

				if (httpServletRequest != null) {

					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());

					WSWebAuthenticationDetailsSource wsDetailsSource = new WSWebAuthenticationDetailsSource(
							httpServletRequest);
					wsDetailsSource.setAuthenticatedUser(userService.getUserByUsername(userName));

					usernamePasswordAuthenticationToken.setDetails(wsDetailsSource);
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				} else {
					try {
						Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
								userDetails.getUsername(), userDetails.getPassword()));
						SecurityContextHolder.getContext().setAuthentication(authentication);
					} catch (Exception ex) {
						throw ex;
					}
				}
			}
		}

	}
	
	public void authenticate(String username, String password) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				username, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public void revoke() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}
}
