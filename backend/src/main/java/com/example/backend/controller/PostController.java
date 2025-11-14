package com.example.backend.controller;


import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.PostDto;
import com.example.backend.dto.PostResponseDto;
import com.example.backend.dto.ProfileDto;
import com.example.backend.model.Comment;
import com.example.backend.model.Posts;
import com.example.backend.model.User;
import com.example.backend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;


    @PostMapping("/create_post")
    public ResponseEntity<PostDto> createPost(
            @RequestParam("body") String body,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestHeader("Authorization") String authHeader
    ) throws IOException {
        return ResponseEntity.ok(postService.createPost(file, body, authHeader));
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostResponseDto>> getAllPosts(){
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("/{pId}/deletePost")
    public ResponseEntity<String> deletePost(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable("pId") String postId){
        return ResponseEntity.ok(postService.deletePost(authHeader,postId));
    }

    @PostMapping("/{pId}/update_post")
    public ResponseEntity<PostDto> updatePost(@PathVariable("pId") String pId,
                                              @RequestHeader("Authorization") String authHeader,
                                              @RequestParam("body") String body,
                                              @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(postService.updatePost(authHeader,file,body,pId));
    }


    @GetMapping("/{pId}/getComments")
    public ResponseEntity<List<CommentResponseDto>> getCommentByPost(@PathVariable("pId") String pId){
        return ResponseEntity.ok(postService.getCommentByPost(pId));
    }

    @PostMapping("/{pId}/comment")
    public ResponseEntity<String> comment(@PathVariable("pId") String pId,
                                    @RequestParam("body") String comment,
                                    @RequestHeader("Authorization") String authHeader){
        return ResponseEntity.ok(postService.commentPost(pId,comment,authHeader));

    }

    @PostMapping("/{commentId}/deleteComment")
    public ResponseEntity<String> deleteComment(
            @PathVariable("commentId") String commentId,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(postService.deleteComment(commentId, authHeader));
    }


    @PostMapping("/{pId}/likeIncrement")
    public ResponseEntity<String> incrementLikes(@PathVariable("pId") String pId) {
        return ResponseEntity.ok(postService.incrementLikes(pId));
    }

}
