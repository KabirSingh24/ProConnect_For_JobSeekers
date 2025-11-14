package com.example.backend.repo;

import com.example.backend.model.Profile;
import com.example.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepo extends MongoRepository<Profile,String> {

    Profile findByUser(User user);

    Profile findByUserId(String userId);
}
