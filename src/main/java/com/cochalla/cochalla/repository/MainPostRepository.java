package com.cochalla.cochalla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import com.cochalla.cochalla.domain.Post;

public interface MainPostRepository extends JpaRepository<Post,Integer> {
    List<Post> findAllByOrderByPostIdDesc(Pageable pageable); // 최신 순
    
}
