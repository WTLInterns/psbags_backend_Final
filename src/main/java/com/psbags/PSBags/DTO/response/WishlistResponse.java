package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishlistResponse {
    private int wishlistId;
    private int productId;
    private String productName;
    private String productImage;
    private String productPrice;

}
