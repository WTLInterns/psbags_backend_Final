package com.psbags.PSBags.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.psbags.PSBags.Service.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{

	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("==================== DEBUG START - JWT FILTER ====================");
		System.out.println("[JWT FILTER START]");
		System.out.println("[JWT FILTER] Request URI: " + request.getRequestURI());
		System.out.println("[JWT FILTER] Request Method: " + request.getMethod());
		System.out.println("[JWT FILTER] Request Origin: " + request.getHeader("Origin"));
		System.out.println("[JWT FILTER] Request Referer: " + request.getHeader("Referer"));
		
		final String authHeader = request.getHeader("Authorization");
		System.out.println("[JWT FILTER] Authorization Header: " + (authHeader != null ? authHeader.substring(0, Math.min(50, authHeader.length())) + "..." : "NULL"));
		
		final String  jwtToken;
		final String email;
		
		if(authHeader == null || authHeader.isBlank()) {
			System.out.println("[JWT FILTER] JWT Found: NO");
			System.out.println("[JWT FILTER] ⚠️ No Authorization header - passing request through");
			System.out.println("[JWT FILTER] Authentication Created: NO");
			System.out.println("[JWT FILTER] Security Context Updated: NO");
			System.out.println("[JWT FILTER END]");
			System.out.println("==================== DEBUG END - JWT FILTER ====================");
			filterChain.doFilter(request, response);
			return;
		}
		
		System.out.println("[JWT FILTER] JWT Found: YES");
		jwtToken = authHeader.substring(7);
		System.out.println("[JWT FILTER] JWT Token (first 30 chars): " + jwtToken.substring(0, Math.min(30, jwtToken.length())) + "...");
		
		try {
			email = jwtUtils.extractUsername(jwtToken);
			System.out.println("[JWT FILTER] Username extracted from JWT: " + email);
			System.out.println("[JWT FILTER] JWT Valid: " + (email != null ? "CHECKING..." : "INVALID"));
			
			if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				System.out.println("[JWT FILTER] Current Security Context Authentication: NULL");
				System.out.println("[JWT FILTER] Loading user details for: " + email);
				
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
				System.out.println("[JWT FILTER] User details loaded: " + userDetails.getUsername());
				System.out.println("[JWT FILTER] User authorities: " + userDetails.getAuthorities());
				
				if(jwtUtils.isTokenValid(jwtToken, userDetails)) {
					System.out.println("[JWT FILTER] JWT Valid: YES");
					System.out.println("[JWT FILTER] Token Validation Result: PASSED");
					
					SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
					token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					securityContext.setAuthentication(token);
					SecurityContextHolder.setContext(securityContext);
					
					System.out.println("[JWT FILTER] Authentication Created: YES");
					System.out.println("[JWT FILTER] Authentication Object: " + token);
					System.out.println("[JWT FILTER] Authenticated User: " + userDetails.getUsername());
					System.out.println("[JWT FILTER] Security Context Updated: YES");
				} else {
					System.out.println("[JWT FILTER] JWT Valid: NO");
					System.out.println("[JWT FILTER] Token Validation Result: FAILED");
					System.out.println("[JWT FILTER] Authentication Created: NO");
				}
			} else if (email != null) {
				System.out.println("[JWT FILTER] Security Context already has authentication: " + SecurityContextHolder.getContext().getAuthentication());
			}
		} catch (Exception e) {
			System.err.println("[JWT FILTER] ❌ Exception during JWT processing: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("[JWT FILTER END]");
		System.out.println("==================== DEBUG END - JWT FILTER ====================");
		filterChain.doFilter(request, response);
	}
}
