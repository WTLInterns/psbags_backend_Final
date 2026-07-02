package com.psbags.PSBags.DTO.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnquiryRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name cannot exceed 150 characters")
    private String fullName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobile;

    @NotBlank(message = "Company name is required")
    @Size(max = 200, message = "Company name cannot exceed 200 characters")
    private String companyName;

    @NotBlank(message = "Product requirement is required")
    @Size(max = 500, message = "Product requirement cannot exceed 500 characters")
    private String productRequirement;

    @NotBlank(message = "Location is required")
    @Size(max = 200, message = "Location cannot exceed 200 characters")
    private String location;

    @NotBlank(message = "Product type is required")
    @Size(max = 100, message = "Product type cannot exceed 100 characters")
    private String productType;

    @NotBlank(message = "Product count is required")
    @Size(max = 50, message = "Product count cannot exceed 50 characters")
    private String productCount;
}
