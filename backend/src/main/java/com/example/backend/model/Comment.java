package com.example.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comment")
public class Comment {

    @Id
    private String id;
    private String userId;
    private String postId;

    @NotBlank(message = "Comments Required")
    private String body;
}
