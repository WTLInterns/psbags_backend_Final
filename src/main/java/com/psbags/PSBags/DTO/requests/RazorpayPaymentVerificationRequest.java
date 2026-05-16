package com.psbags.PSBags.DTO.requests;

import lombok.Data;

@Data
public class RazorpayPaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
