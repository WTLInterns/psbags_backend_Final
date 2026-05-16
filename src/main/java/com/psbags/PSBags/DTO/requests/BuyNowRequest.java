package com.psbags.PSBags.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyNowRequest {
    
    private int productId;
    private int quantity;
    private String size;
}
