package com.example.server.service;

import com.example.server.entity.Comment;
import com.example.server.entity.Post;
import com.example.server.entity.User;
import com.example.server.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Post post, User user, String text, Integer parentCommentId) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(text);
        if (parentCommentId != null) {
            Comment parent = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parent);
        }
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}