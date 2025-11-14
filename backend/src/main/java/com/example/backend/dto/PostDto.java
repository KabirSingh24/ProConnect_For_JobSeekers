package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Builder
public class PostDto {

    @NotBlank(message = "Body is required")
    private String body;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
    private String fileUrl;
    private String fileType;


}
