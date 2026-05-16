package com.psbags.PSBags.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequests {
    
    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String role;

    private String provider;


}
