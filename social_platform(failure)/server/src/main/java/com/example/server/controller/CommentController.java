package com.example.server.controller;

import com.example.server.entity.Comment;
import com.example.server.entity.Post;
import com.example.server.entity.User;
import com.example.server.service.CommentService;
import com.example.server.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Integer postId,
            @RequestBody Map<String, Object> data,
            @AuthenticationPrincipal User user) {
        try {
            Post post = postService.getPostById(postId);
            String text = (String) data.get("text");
            Integer parentCommentId = data.get("parentCommentId") != null ?
                    Integer.parseInt(data.get("parentCommentId").toString()) : null;
            Comment comment = commentService.createComment(post, user, text, parentCommentId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "commentId", comment.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Integer postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "comments", comments.stream().map(comment -> Map.of(
                            "id", comment.getId(),
                            "post_id", comment.getPost().getId(),
                            "user_id", comment.getUser().getId(),
                            "name", comment.getUser().getName(),
                            "avatar", comment.getUser().getAvatar() != null ? comment.getUser().getAvatar() : "",
                            "text", comment.getText(),
                            "created_at", comment.getCreatedAt().toString(),
                            "parent_comment_id", comment.getParentComment() != null ? comment.getParentComment().getId() : null
                    )).toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}