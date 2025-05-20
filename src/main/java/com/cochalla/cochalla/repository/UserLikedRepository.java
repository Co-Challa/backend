package com.cochalla.cochalla.repository;

import com.cochalla.cochalla.dto.UserLikeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import com.cochalla.cochalla.domain.Like;
import com.cochalla.cochalla.domain.LikeId;

public interface UserLikedRepository extends JpaRepository<Like, LikeId> {

    boolean existsByUserUserIdAndPostPostId(String userId, Integer postId);

    @Query("SELECT new com.cochalla.cochalla.dto.UserLikeDto(" +
           "p.postId, s.title, s.content, s.createdAt, " +
           "size(p.comments), size(p.likes), p.isPublic, " +
           "u.nickname, u.profileImg) " +
           "FROM Like l " +
           "JOIN l.post p " +
           "JOIN p.summary s " +
           "JOIN p.user u " +
           "WHERE l.user.userId = :userId")
    Page<UserLikeDto> findUserLikedPosts(@Param("userId") String userId, Pageable pageable);
}