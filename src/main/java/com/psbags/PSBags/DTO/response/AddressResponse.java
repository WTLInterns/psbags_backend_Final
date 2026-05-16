package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    

    private int id;
    private String steet;

    private String city;

    private String landmark;

    private String pincode;

    private String address;

}
