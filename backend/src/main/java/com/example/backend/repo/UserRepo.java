package com.example.backend.repo;

import com.example.backend.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User,String> {

    User findByEmail(String email);
    User findByUsername(String username);

    User findByName(@NotBlank(message = "User Name Must be Unique and required") String name);
}
