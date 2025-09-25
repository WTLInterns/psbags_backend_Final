package com.garja.Garja.OAuth2;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.garja.Garja.JWT.JwtUtils;
import com.garja.Garja.Model.User;
import com.garja.Garja.Service.OAuth2UserService;

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

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // Process and save user data
        User user = oAuth2UserService.processOAuth2User(oAuth2User);
        
        // Generate JWT token
        String token = jwtUtils.generateToken(user);
        
        // Redirect to frontend with token
        String targetUrl = "http://localhost:3000/auth/callback?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
        
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
