package com.psbags.PSBags.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BlogResponse {
    
    private int id;
    
    private String title;
    
    private String message;
    
    // Constructor matching ProductResponse pattern
    public BlogResponse(int id, String message, String title) {
        this.id = id;
        this.message = message;
        this.title = title;
    }
}