package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "posts")
public class Posts {

    @Id
    private String id;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @NotBlank(message = "Body is required")
    private String body;

    @Builder.Default
    private int likes=0;

    private String fileUrl; // S3 or uploaded file

    @Builder.Default
    private boolean active = true;

    private String fileType;

    private String userId;
}
