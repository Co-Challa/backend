package com.cochalla.cochalla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.Like;
import com.cochalla.cochalla.domain.LikeId;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    
}
