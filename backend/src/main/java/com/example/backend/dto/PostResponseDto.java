package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private String postId;
    private String body;
    private String fileUrl;
    private String fileType;
    private int likes;
    private Date createdAt;

    private UserInfoDto user; // nested user info
}