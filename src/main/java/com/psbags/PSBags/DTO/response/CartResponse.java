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
    private double totalAmount;
    private int totalItems;
}
