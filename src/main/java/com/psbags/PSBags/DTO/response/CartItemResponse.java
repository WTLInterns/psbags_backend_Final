package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    
    private Long id;
    private int quantity;
    private double lineTotal;
    private String size;
    
    // Product details
    private int productId;
    private String productName;
    private String price;
    private String imageUrl;
    private String category;
    private String isActive;
    private String shippingType;
    private Double shippingCost;
}
