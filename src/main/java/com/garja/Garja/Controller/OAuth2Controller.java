package com.garja.Garja.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.garja.Garja.JWT.JwtUtils;
import com.garja.Garja.Model.User;
import com.garja.Garja.Service.OAuth2UserService;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class OAuth2Controller {

    @Autowired
    private OAuth2UserService oAuth2UserService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        System.out.println("OAuth2Controller: /auth/google endpoint called");
        // This will redirect to Google OAuth2 authorization endpoint with account selection
        String redirectUrl = "/oauth2/authorization/google?prompt=select_account";
        System.out.println("Redirecting to: " + redirectUrl);
        response.sendRedirect(redirectUrl);
    }
    
    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("OAuth2Controller: /auth/google/callback endpoint called");
            System.out.println("Request data: " + request);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> userInfo = (Map<String, Object>) request.get("userInfo");
            
            if (userInfo == null) {
                return ResponseEntity.badRequest().body("No user info provided");
            }
            
            // Create a mock OAuth2User from the userInfo
            MockOAuth2User mockUser = new MockOAuth2User(userInfo);
            
            // Process the user (create or update)
            User user = oAuth2UserService.processOAuth2User(mockUser);
            
            // Generate JWT token
            String token = jwtUtils.generateToken(user);
            
            // Return the token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Authentication successful");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error in Google OAuth callback: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
        }
    }
    
    @GetMapping("/oauth2/success")
    public ResponseEntity<String> oauth2Success() {
        System.out.println("OAuth2Controller: /auth/oauth2/success endpoint called");
        return ResponseEntity.ok("OAuth2 login successful - Backend is running!");
    }
    
    // Mock OAuth2User implementation for direct Google API integration
    private static class MockOAuth2User implements org.springframework.security.oauth2.core.user.OAuth2User {
        private final Map<String, Object> attributes;
        
        public MockOAuth2User(Map<String, Object> userInfo) {
            this.attributes = new HashMap<>();
            this.attributes.put("sub", userInfo.get("id"));
            this.attributes.put("email", userInfo.get("email"));
            this.attributes.put("given_name", userInfo.get("given_name"));
            this.attributes.put("family_name", userInfo.get("family_name"));
            this.attributes.put("picture", userInfo.get("picture"));
        }
        
        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }
        
        @Override
        public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
            return java.util.Collections.emptyList();
        }
        
        @Override
        public String getName() {
            return (String) attributes.get("email");
        }
        
        @SuppressWarnings("unchecked")
        public <A> A getAttribute(String name) {
            return (A) this.attributes.get(name);
        }
    }
}
