package com.example.backend.config;

import com.example.backend.model.User;
import com.example.backend.repo.UserRepo;
import com.example.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {


    private final JwtService jwtService;
    private final UserRepo userRepo;

    public User getCurrentUser(String authHeader){
        String token=authHeader.replace("Bearer ","");
        String username=jwtService.extractUsername(token);
        User user=userRepo.findByUsername(username);
        if(user==null){
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }
}
