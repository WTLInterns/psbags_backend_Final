package com.psbags.PSBags.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.UserRepo;

@Service
public class OAuth2UserService {

    @Autowired
    private UserRepo userRepo;

    public User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String providerId = oAuth2User.getAttribute("sub");
        String profileImageUrl = oAuth2User.getAttribute("picture");
        
        // Check if user already exists
        Optional<User> existingUser = userRepo.findByEmail(email);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Update OAuth2 info if it's a new OAuth2 login for existing user
            if (user.getProvider() == null || !user.getProvider().equals("google")) {
                user.setProvider("google");
                user.setProviderId(providerId);
                user.setProfileImageUrl(profileImageUrl);
                return userRepo.save(user);
            }
            return user;
        } else {
            // Create new user
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFirstName(firstName != null ? firstName : "");
            newUser.setLastName(lastName != null ? lastName : "");
            newUser.setProvider("google");
            newUser.setProviderId(providerId);
            newUser.setProfileImageUrl(profileImageUrl);
            newUser.setRole("USER");
            // No password needed for OAuth2 users
            newUser.setPassword(null);
            // Phone number will be null initially, user can update later
            newUser.setPhoneNumber(null);
            
            return userRepo.save(newUser);
        }
    }
}
