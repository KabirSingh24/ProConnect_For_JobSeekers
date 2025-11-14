package com.example.backend.dto;

import com.example.backend.model.Profile;
import com.example.backend.model.User;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseWithProfile {
    public Profile profile;
}
