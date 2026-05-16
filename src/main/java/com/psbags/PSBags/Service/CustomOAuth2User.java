package com.psbags.PSBags.Service;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.psbags.PSBags.Model.User;

public class CustomOAuth2User implements OAuth2User {
    
    private OAuth2User oAuth2User;
    private User user;
    
    public CustomOAuth2User(OAuth2User oAuth2User, User user) {
        this.oAuth2User = oAuth2User;
        this.user = user;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }
    
    @Override
    public String getName() {
        return oAuth2User.getAttribute("email");
    }
    
    public User getUser() {
        return user;
    }
}
