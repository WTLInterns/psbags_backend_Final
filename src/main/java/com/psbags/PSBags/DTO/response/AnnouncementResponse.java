package com.psbags.PSBags.DTO.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AnnouncementResponse {
    private Long id;
    private String text1;
    private String text2;
    private String text3;
    private String text4;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // For public API - only active announcements as array
    private List<String> announcements;
}