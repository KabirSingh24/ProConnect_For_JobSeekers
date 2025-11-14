package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String id;
    private String body;
    private String postId;
    private String userId;
    private String name;
    private String username;
    private String profilePicture; // optional
}
