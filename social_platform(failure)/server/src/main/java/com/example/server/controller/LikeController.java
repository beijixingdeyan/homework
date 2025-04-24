package com.example.server.controller;

import com.example.server.entity.Post;
import com.example.server.entity.User;
import com.example.server.service.LikeService;
import com.example.server.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/likes")
public class LikeController {
    private final LikeService likeService;
    private final PostService postService;

    public LikeController(LikeService likeService, PostService postService) {
        this.likeService = likeService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> likePost(
            @PathVariable Integer postId,
            @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(postId);
            likeService.likePost(post, user);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}