package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    
    private Long id;
    private int userId;
    private List<CartItemResponse> items;
    private double subtotal;
    private double highestShipping;
    private double gstPercentage;
    private double gstAmount;
    private double grandTotal;
    private int totalItems;

    // Legacy field kept for backward compatibility
    public double getTotalAmount() { return grandTotal; }
}
