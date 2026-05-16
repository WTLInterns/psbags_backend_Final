package com.psbags.PSBags.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.DTO.requests.PasswordRequests;
import com.psbags.PSBags.DTO.response.PasswordResponse;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.UserRepo;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    


    public PasswordResponse resetPassword(String email, PasswordRequests request) {
        User user = this.userRepo.findByEmail(email).orElseThrow();
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepo.save(user);

        return new PasswordResponse(email, "Password reset successfully!");
    }
}
