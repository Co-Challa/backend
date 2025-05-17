package com.cochalla.cochalla.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.PostDto;


public interface PostRepository extends JpaRepository<Post, Integer>{
    @Query("SELECT new com.cochalla.cochalla.dto.PostDto(" +
           "p.postId, p.isPublic, s.title, s.content, s.createdAt, " +
           "u.userId, u.nickname, u.profileImg, SIZE(p.likes), SIZE(p.comments)) " +
           "FROM Post p JOIN p.summary s JOIN p.user u WHERE p.postId = :postId")
    Optional<PostDto> findPostById(@Param("postId") Integer postId);

    Optional<Post> findByPostIdAndUser_userId(Integer postId, String userId);

    Boolean existsByPostIdAndUser_userId(Integer postId, String userId);

    List<Post> findAllByOrderByPostIdDesc(Pageable pageable); 
}
