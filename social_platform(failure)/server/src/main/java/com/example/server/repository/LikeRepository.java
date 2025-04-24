package com.example.server.repository;

import com.example.server.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    long countByPostId(Integer postId);
    boolean existsByPostIdAndUserId(Integer postId, Integer userId);
}