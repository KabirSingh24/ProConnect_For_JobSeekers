package com.example.backend.dto;

import com.example.backend.model.Education;
import com.example.backend.model.Work;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileResponse {
    private String id;
    private String name;
    private String username;
    private String email;
    private String profilePicture;
    private String bio;
    private String currentPost;
    private List<Work> pastWork;
    private List<Education> education;
}
