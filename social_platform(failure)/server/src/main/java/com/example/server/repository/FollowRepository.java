package com.example.server.repository;

import com.example.server.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    boolean existsByFollowerUserIdAndFollowedUserId(Integer followerUserId, Integer followedUserId);
}