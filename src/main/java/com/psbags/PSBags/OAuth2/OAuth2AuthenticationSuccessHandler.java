package com.psbags.PSBags.OAuth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.psbags.PSBags.JWT.JwtUtils;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Service.OAuth2UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        System.out.println("==================== DEBUG START - OAUTH2 SUCCESS HANDLER ====================");
        System.out.println("[OAUTH2 SUCCESS]");
        System.out.println("[OAUTH2] Authentication successful");
        System.out.println("[OAUTH2] Request URI: " + request.getRequestURI());
        System.out.println("[OAUTH2] Request Method: " + request.getMethod());
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("[OAUTH2] OAuth2 User: " + oAuth2User.getName());
        System.out.println("[OAUTH2] OAuth2 Attributes: " + oAuth2User.getAttributes());

        // Process and save user data
        System.out.println("[OAUTH2] Processing OAuth2 user data...");
        User user = oAuth2UserService.processOAuth2User(oAuth2User);
        System.out.println("[OAUTH2] User processed - ID: " + user.getId() + ", Email: " + user.getEmail());

        // Generate JWT token
        System.out.println("[OAUTH2] Generating JWT token...");
        String token = jwtUtils.generateToken(user);
        System.out.println("[OAUTH2] JWT token generated: " + token.substring(0, Math.min(30, token.length())) + "...");

        // Redirect to frontend with token
        String targetUrl = "http://localhost:8086/auth/callback?token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8);
        
        System.out.println("[OAUTH2] Redirect Target URL: " + targetUrl);
        System.out.println("[OAUTH2] Redirecting To: " + targetUrl);
        System.out.println("[OAUTH2] ✅ Sending redirect response");
        System.out.println("==================== DEBUG END - OAUTH2 SUCCESS HANDLER ====================");

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
