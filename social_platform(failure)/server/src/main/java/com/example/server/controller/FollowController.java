package com.example.server.controller;

import com.example.server.entity.User;
import com.example.server.service.FollowService;
import com.example.server.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/{userId}/follow")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> followUser(
            @PathVariable Integer userId,
            @AuthenticationPrincipal User follower) {
        try {
            User followee = userService.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            followService.followUser(follower, followee);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}