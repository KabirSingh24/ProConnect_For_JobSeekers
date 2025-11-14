package com.example.backend.service;

import com.example.backend.config.CurrentUserUtil;
import com.example.backend.dto.*;
import com.example.backend.model.Comment;
import com.example.backend.model.Posts;
import com.example.backend.model.Profile;
import com.example.backend.model.User;
import com.example.backend.repo.CommentRepo;
import com.example.backend.repo.PostRepo;
import com.example.backend.repo.UserRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepo postRepo;
    private final CurrentUserUtil currentUserUtil;
    private final UserRepo userRepo;
    private final FileService fileService;
    private final CommentRepo commentRepo;

    public PostDto createPost(MultipartFile file, String body, String authHeader) throws IOException {
        User user = currentUserUtil.getCurrentUser(authHeader);

        String fileUrl = "";
        String fileType = "";

        if (file != null && !file.isEmpty()) {
            fileUrl = fileService.saveFile(file);
            fileType = file.getContentType();
        }

        Posts post = Posts.builder()
                .createdAt(new Date(System.currentTimeMillis()))
                .body(body)
                .createdAt(new Date(System.currentTimeMillis()))
                .fileUrl(fileUrl)
                .fileType(fileType)
                .userId(user.getId())
                .build();

        postRepo.save(post);

        return PostDto.builder()
                .body(post.getBody())
                .fileUrl(post.getFileUrl())
                .fileType(post.getFileType())
                .build();
    }

    public List<PostResponseDto> getAllPosts() {

        List<Posts> posts = postRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        List<PostResponseDto> response = new ArrayList<>();

        for (Posts post : posts) {
            User postUser = userRepo.findById(post.getUserId()).orElse(null);
            if (postUser == null) continue;

            PostResponseDto dto = PostResponseDto.builder()
                    .postId(post.getId())
                    .body(post.getBody())
                    .fileUrl(post.getFileUrl())
                    .fileType(post.getFileType())
                    .likes(post.getLikes())
                    .createdAt(post.getCreatedAt())
                    .user(UserInfoDto.builder()
                            .id(postUser.getId())
                            .name(postUser.getName())
                            .username(postUser.getUsername())
                            .profilePicture(postUser.getProfilePicture())
                            .build())
                    .build();

            response.add(dto);
        }

        return response;
    }

    public String deletePost(String authHeader,String pId) {
        User user=currentUserUtil.getCurrentUser(authHeader);
        Posts post=postRepo.findById(pId).orElse(null);

        if(post==null){throw new IllegalArgumentException("Post Not Found");}

        if(!post.getUserId().equals(user.getId())){
            throw new IllegalArgumentException("User is not Authorized");
        }
        postRepo.delete(post);
        return "Post Deleted Successfully";
    }

    public String commentPost(String pId,String comment,String authHeader){
        User user=currentUserUtil.getCurrentUser(authHeader);
        Posts post = postRepo.findById(pId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Prevent commenting on own post
        if (user.getId().equals(post.getUserId())) {
            throw new IllegalArgumentException("Comment on own post is not allowed");
        }

        Comment cmt= Comment.builder()
                .postId(pId)
                .userId(user.getId())
                .body(comment!=null?comment:"")
                .build();
        commentRepo.save(cmt);
        return "Comment Saved";
    }
    public PostDto updatePost(String authHeader,MultipartFile file,String body,String pId) throws IOException {
        User user=currentUserUtil.getCurrentUser(authHeader);
        Posts post = postRepo.findById(pId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }

        post.setBody(body);
        if (file != null && !file.isEmpty()) {
            post.setFileUrl(fileService.saveFile(file));
            post.setFileType(file.getContentType());
        }
        post.setUpdatedAt(new Date());
        postRepo.save(post);

        return PostDto.builder()
                .body(post.getBody())
                .fileUrl(post.getFileUrl())
                .fileType(post.getFileType())
                .build();


    }

    public List<CommentResponseDto> getCommentByPost(String pId) {
        List<Comment> comments = commentRepo.findByPostId(pId);

        return comments.stream().map(c -> {
            User user = userRepo.findById(c.getUserId()).orElse(null);
            return CommentResponseDto.builder()
                    .id(c.getId())
                    .body(c.getBody())
                    .postId(c.getPostId())
                    .userId(c.getUserId())
                    .name(user!=null ? user.getName():"Unknown")
                    .username(user != null ? user.getUsername() : "Unknown")
                    .profilePicture(user != null ? user.getProfilePicture() : null)
                    .build();
        }).collect(Collectors.toList());
    }


    public String deleteComment(String commentId, String authHeader) {
        User user = currentUserUtil.getCurrentUser(authHeader);
        Comment comment = commentRepo.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("Comment not found")
        );

        if (!comment.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You cannot delete this comment");
        }

        commentRepo.delete(comment);
        return "Comment Deleted";
    }


    public String incrementLikes(String pId){
        Posts posts=postRepo.findById(pId).orElse(null);
        if(posts==null){
            throw new IllegalArgumentException("Post not found");
        }
        posts.setLikes(posts.getLikes() + 1);
        postRepo.save(posts);
        return "Like Added";
    }
}
