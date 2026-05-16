package com.psbags.PSBags.DTO.requests;

import lombok.Data;

@Data
public class RazorpayOrderRequest {
    private double amount;
    private String currency = "INR";
    private String receipt;
}
