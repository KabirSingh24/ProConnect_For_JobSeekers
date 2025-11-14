package com.example.backend.repo;

import com.example.backend.model.Education;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepo extends MongoRepository<Education,String> {
}
