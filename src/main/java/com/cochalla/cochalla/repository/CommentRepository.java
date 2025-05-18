package com.cochalla.cochalla.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.dto.CommentDto;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    
    @Query("SELECT new com.cochalla.cochalla.dto.CommentDto(" +
           "c.postCommentId, c.content, c.createdAt, u.userId, u.nickname, u.profileImg) " +
           "FROM Comment c JOIN c.user u WHERE c.post.postId = :postId ORDER BY c.createdAt ASC")
    List<CommentDto> findCommentsByPostId(@Param("postId") Integer postId);
}
