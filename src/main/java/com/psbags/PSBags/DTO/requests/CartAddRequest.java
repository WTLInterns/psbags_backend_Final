package com.psbags.PSBags.DTO.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartAddRequest {
    @NotNull(message = "productId is required")
    private Integer productId;

    @Min(value = 1, message = "quantity must be at least 1")
    private Integer quantity = 1;
}
