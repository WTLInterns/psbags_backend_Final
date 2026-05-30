package com.psbags.PSBags.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryRequest {
    
    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Subcategory name is required")
    private String subcategoryName;
}