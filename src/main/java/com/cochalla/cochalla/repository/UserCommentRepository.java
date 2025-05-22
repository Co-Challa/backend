package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findByUserUserId(String userId, Pageable pageable);
}
