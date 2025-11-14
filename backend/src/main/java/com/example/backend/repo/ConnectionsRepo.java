package com.example.backend.repo;

import com.example.backend.model.ConnectionRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionsRepo extends MongoRepository<ConnectionRequest,String> {
    boolean existsByUserIdAndConnectionId(String userId, String connectionId);

    List<ConnectionRequest> findByUserId(String userId);

    List<ConnectionRequest> findByConnectionId(String userid);
}
