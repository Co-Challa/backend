package com.cochalla.cochalla.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.dto.CommentResponseDto;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
    
    @Query("""
        SELECT new com.cochalla.cochalla.dto.CommentResponseDto(
        c.postCommentId, c.content, c.createdAt, u.userId, u.nickname, u.profileImg)
        FROM Comment c JOIN c.user u WHERE c.post.postId = :postId
    """)
    Page<CommentResponseDto> findCommentsByPostId(@Param("postId") Integer postId, Pageable pageable);

    @Query("""
        SELECT new com.cochalla.cochalla.dto.CommentResponseDto(
        c.postCommentId, c.content, c.createdAt, u.userId, u.nickname, u.profileImg)
        FROM Comment c JOIN c.user u WHERE c.user.userId = :userId
    """)
    Page<CommentResponseDto> findCommentsByUserId(@Param("userId") String userId, Pageable pageable);

    @Query("SELECT c.post.postId FROM Comment c WHERE c.postCommentId = :commentId AND c.user.userId = :userId")
    Integer findPostIdByCommentIdAndUserId(@Param("commentId") Integer commentId, @Param("userId") String userId);

    Long countByPost_postId(Integer postId);
}
