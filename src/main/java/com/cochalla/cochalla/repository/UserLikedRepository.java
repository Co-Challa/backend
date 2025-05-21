package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Like;
import com.cochalla.cochalla.domain.LikeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikedRepository extends JpaRepository<Like, LikeId> {
    Page<Like> findByUserUserId(String userId, Pageable pageable);
}
