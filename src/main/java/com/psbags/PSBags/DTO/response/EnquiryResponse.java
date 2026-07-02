package com.psbags.PSBags.DTO.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnquiryResponse {

    private Long id;
    private String fullName;
    private String mobile;
    private String companyName;
    private String productRequirement;
    private String location;
    private String productType;
    private String productCount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Top-level fields for submit success response
    private Boolean success;
    private String message;
}
