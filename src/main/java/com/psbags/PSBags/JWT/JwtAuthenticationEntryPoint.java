package com.psbags.PSBags.JWT;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		System.out.println("==================== DEBUG START - AUTH ENTRY POINT ====================");
		System.out.println("[SECURITY ENTRY]");
		System.out.println("[AUTH ENTRY POINT] Authentication failed - commence called");
		System.out.println("[AUTH ENTRY POINT] Incoming URL: " + request.getRequestURI());
		System.out.println("[AUTH ENTRY POINT] Request Method: " + request.getMethod());
		System.out.println("[AUTH ENTRY POINT] Origin: " + request.getHeader("Origin"));
		System.out.println("[AUTH ENTRY POINT] Referer: " + request.getHeader("Referer"));
		System.out.println("[AUTH ENTRY POINT] Protected Endpoint: YES");
		System.out.println("[AUTH ENTRY POINT] Authentication Required: YES");
		System.out.println("[AUTH ENTRY POINT] Current Authentication: NULL or INVALID");
		System.out.println("[AUTH ENTRY POINT] Exception: " + authException.getMessage());
		System.out.println("[AUTH ENTRY POINT] Returning: 401 UNAUTHORIZED");
		System.out.println("[AUTH ENTRY POINT] NOT redirecting to OAuth2");
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		PrintWriter writer = response.getWriter();
		writer.println("Access Denied with your error message" + authException.getMessage());
		
		System.out.println("[AUTH ENTRY POINT] ✅ Response sent");
		System.out.println("==================== DEBUG END - AUTH ENTRY POINT ====================");
	}

}
