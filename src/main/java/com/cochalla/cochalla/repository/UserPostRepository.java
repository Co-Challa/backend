package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByUserUserId(String userId, Pageable pageable);
}