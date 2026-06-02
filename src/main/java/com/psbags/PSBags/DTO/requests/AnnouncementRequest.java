package com.psbags.PSBags.DTO.requests;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class AnnouncementRequest {
    
    @Size(max = 500, message = "Text1 cannot exceed 500 characters")
    private String text1;
    
    @Size(max = 500, message = "Text2 cannot exceed 500 characters")
    private String text2;
    
    @Size(max = 500, message = "Text3 cannot exceed 500 characters")
    private String text3;
    
    @Size(max = 500, message = "Text4 cannot exceed 500 characters")
    private String text4;
    
    private Boolean isActive = true;
}