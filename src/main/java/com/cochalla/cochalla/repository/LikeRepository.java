package com.cochalla.cochalla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.Like;
import com.cochalla.cochalla.domain.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    Long countByPost_postId(Integer postId);
    Boolean existsByUser_userId(String userId);
    Boolean existsByUser_userIdAndPost_postId(String userId, Integer postId);
}
