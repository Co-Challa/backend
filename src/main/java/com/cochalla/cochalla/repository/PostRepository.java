package com.cochalla.cochalla.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cochalla.cochalla.domain.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
    
}
