package com.example.backend.model;


import com.example.backend.model.type.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "connectionRequest")
public class ConnectionRequest {

    @Id
    private String id;

    private String userId;         // The current user who sent/has connection
    private String connectionId;

    @Builder.Default// The user they’re connected to
    private Status accepted=Status.PENDING;      // true if accepted, false if pending
}
