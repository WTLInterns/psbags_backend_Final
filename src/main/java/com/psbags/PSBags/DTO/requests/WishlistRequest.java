package com.psbags.PSBags.DTO.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WishlistRequest {
    @NotNull(message = "productId is required")
    private Integer productId;
}
