package com.example.server.controller;

import com.example.server.entity.Post;
import com.example.server.entity.User;
import com.example.server.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final FileService fileService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final ObjectMapper objectMapper;

    public PostController(PostService postService, FileService fileService, LikeService likeService,
                          CommentService commentService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.fileService = fileService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @AuthenticationPrincipal User user) {
        try {
            String imagesJson = "[]";
            if (images != null && images.length > 0) {
                List<String> imageUrls = Arrays.stream(images)
                        .map(file -> {
                            try {
                                return fileService.uploadImage(file);
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to upload image: " + e.getMessage());
                            }
                        })
                        .toList();
                imagesJson = objectMapper.writeValueAsString(imageUrls);
            }
            Post post = postService.createPost(user, content, imagesJson);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "postId", post.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getPosts(@AuthenticationPrincipal User user) {
        try {
            List<Post> posts = postService.getAllPosts();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "posts", posts.stream().map(post -> Map.of(
                            "id", post.getId(),
                            "user_id", post.getUser().getId(),
                            "name", post.getUser().getName(),
                            "avatar", post.getUser().getAvatar() != null ? post.getUser().getAvatar() : "",
                            "content", post.getContent(),
                            "images", post.getImages(),
                            "created_at", post.getCreatedAt().toString(),
                            "likes_count", likeService.getLikeCount(post.getId()),
                            "liked", user != null && likeService.isLikedByUser(post.getId(), user.getId()),
                            "comments_count", commentService.getCommentsByPostId(post.getId()).size()
                    )).toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPost(@PathVariable Integer id, @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(id);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "post", Map.of(
                            "id", post.getId(),
                            "user_id", post.getUser().getId(),
                            "name", post.getUser().getName(),
                            "avatar", post.getUser().getAvatar() != null ? post.getUser().getAvatar() : "",
                            "content", post.getContent(),
                            "images", post.getImages(),
                            "created_at", post.getCreatedAt().toString(),
                            "likes_count", likeService.getLikeCount(post.getId()),
                            "liked", user != null && likeService.isLikedByUser(post.getId(), user.getId()),
                            "comments_count", commentService.getCommentsByPostId(post.getId()).size()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}