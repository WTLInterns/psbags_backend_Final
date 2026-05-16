package com.psbags.PSBags.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    
    private String steet;

    private String city;

    private String landmark;

    private String pincode;

    private String address;
}



