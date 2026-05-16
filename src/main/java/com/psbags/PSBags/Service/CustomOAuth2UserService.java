package com.psbags.PSBags.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.Model.User;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // Process and save user data
        User user = oAuth2UserService.processOAuth2User(oAuth2User);
        
        // Return OAuth2User with additional user information
        return new CustomOAuth2User(oAuth2User, user);
    }
}
