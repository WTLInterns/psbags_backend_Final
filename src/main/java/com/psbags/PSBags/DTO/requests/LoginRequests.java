package com.psbags.PSBags.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequests {
    
    private String email;

    private String password;
}
