package com.psbags.PSBags.DTO.requests;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {

    private String title;

    private String slug;

    private String description;

    private MultipartFile thumbnail;

    private MultipartFile video;

    private String isActive;

    private String date;

    private String time;
}