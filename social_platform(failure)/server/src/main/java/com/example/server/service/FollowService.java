package com.example.server.service;

import com.example.server.entity.Follow;
import com.example.server.entity.User;
import com.example.server.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void followUser(User follower, User followee) {
        if (followRepository.existsByFollowerUserIdAndFollowedUserId(follower.getId(), followee.getId())) {
            throw new RuntimeException("Already following");
        }
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        followRepository.save(follow);
    }
}