package com.example.backend.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message="Name Required")
    private String name;

    @NotBlank(message = "User name Required")
    @Indexed(unique = true)
    private String username;

    @NotBlank(message = "email Required")
    @Indexed(unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Builder.Default
    private boolean active = true;

    @Builder.Default
    private String profilePicture = "https://cdn-icons-png.flaticon.com/512/847/847969.png";

    @CreatedDate
    private Date createdAt;

    private String token;



}
