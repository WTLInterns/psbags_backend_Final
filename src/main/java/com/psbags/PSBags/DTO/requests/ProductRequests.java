package com.psbags.PSBags.DTO.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequests {
    

    private String productName;

    private String price;

     private String originalPrice;

    private String discount;

    private int quantity;

    private String isActive;

    private String description;

    private String XS;

    private String M;

    private String L;

    private String XL;

    private String XXL;

    private MultipartFile image;

    private MultipartFile image2;

    private MultipartFile image3;

    private MultipartFile image4;

    private String date;

    private String time;

    

    private String category;

    private String subcategoryName;
}
