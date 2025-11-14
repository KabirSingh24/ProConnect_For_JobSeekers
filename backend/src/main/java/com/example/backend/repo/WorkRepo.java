package com.example.backend.repo;

import com.example.backend.model.Work;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepo extends MongoRepository<Work,String> {
}
