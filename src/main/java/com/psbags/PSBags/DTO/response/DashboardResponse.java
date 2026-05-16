package com.psbags.PSBags.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    
    private int orderCount;

    private int price;

    private int productCount;

    private int customerCount;



}
