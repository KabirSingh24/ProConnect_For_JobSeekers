package com.example.backend.dto;

import com.example.backend.model.type.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionRequestDto {
    private String id;
    private String userId;
    private String username;
    private String name;
    private String profilePicture;
    private String connectionId;
    private Status accepted=Status.PENDING;
}
