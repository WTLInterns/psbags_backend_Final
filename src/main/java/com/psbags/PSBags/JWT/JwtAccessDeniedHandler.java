package com.psbags.PSBags.JWT;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        System.out.println("==================== DEBUG START - ACCESS DENIED HANDLER ====================");
        System.out.println("[ACCESS DENIED]");
        System.out.println("[ACCESS DENIED] Incoming URL: " + request.getRequestURI());
        System.out.println("[ACCESS DENIED] Request Method: " + request.getMethod());
        System.out.println("[ACCESS DENIED] User Principal: " + request.getUserPrincipal());
        System.out.println("[ACCESS DENIED] Remote User: " + request.getRemoteUser());
        System.out.println("[ACCESS DENIED] Exception: " + accessDeniedException.getMessage());
        System.out.println("[ACCESS DENIED] ⚠️ Authorization DENIED - User lacks required authorities");
        System.out.println("[ACCESS DENIED] This means user is authenticated but doesn't have the required role/authority");
        
        // Set CORS headers
        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        }
        
        // Set response
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", "Forbidden");
        errorDetails.put("message", "You don't have permission to access this resource. Required authority not found.");
        errorDetails.put("status", HttpServletResponse.SC_FORBIDDEN);
        errorDetails.put("path", request.getRequestURI());
        errorDetails.put("timestamp", System.currentTimeMillis());
        
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorDetails));
        
        System.out.println("[ACCESS DENIED] Response Status: 403 FORBIDDEN");
        System.out.println("[ACCESS DENIED] Response Body: " + errorDetails);
        System.out.println("==================== DEBUG END - ACCESS DENIED HANDLER ====================");
    }
}
