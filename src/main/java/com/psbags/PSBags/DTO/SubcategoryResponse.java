package com.psbags.PSBags.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryResponse {
    
    private int id;
    private String categoryName;
    private String subcategoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}