package com.example.backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "profile")
public class Profile {

    @Id
    private String id;

    @DBRef
    private User user;

    private String bio;
    private String currentPost;

    @DBRef
    private List<Work> pastWork=new ArrayList<>();

    @DBRef
    private List<Education> education=new ArrayList<>();

}
