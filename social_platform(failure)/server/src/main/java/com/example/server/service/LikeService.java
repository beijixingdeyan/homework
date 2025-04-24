package com.example.server.service;

import com.example.server.entity.Like;
import com.example.server.entity.Post;
import com.example.server.entity.User;
import com.example.server.repository.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void likePost(Post post, User user) {
        if (likeRepository.existsByPostIdAndUserId(post.getId(), user.getId())) {
            throw new RuntimeException("Already liked");
        }
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    public long getLikeCount(Integer postId) {
        return likeRepository.countByPostId(postId);
    }

    public boolean isLikedByUser(Integer postId, Integer userId) {
        return likeRepository.existsByPostIdAndUserId(postId, userId);
    }
}