package com.example.backend.dto;

import com.example.backend.model.Education;
import com.example.backend.model.Work;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProfileDto {

    private String bio;
    private String currentPost;
    private List<Work> pastWork=new ArrayList<>();
    private List<Education> education=new ArrayList<>();
}
