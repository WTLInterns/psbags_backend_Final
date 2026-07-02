package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    
    private int id;
    private String orderDate;
    private double totalAmount;
    private String status;
    private String productName;
    private int quantity;
    private String size;
    private String image;
    private int userId;
    private String message;

    // Pricing snapshot
    private Double subtotal;
    private Double shippingCost;
    private Double gstPercentage;
    private Double gstAmount;
    private Double grandTotal;
}
