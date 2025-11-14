package com.example.backend.repo;

import com.example.backend.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepo extends MongoRepository<Comment,String> {
    List<Comment> findByPostId(String pId);
    Comment findByUserId(String userId);
}
