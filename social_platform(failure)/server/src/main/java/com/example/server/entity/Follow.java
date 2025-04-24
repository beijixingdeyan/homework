package com.example.server.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_follows")
@Data
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower_user_id", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_user_id", nullable = false)
    private User followee;

    private LocalDateTime createdAt;
}