package com.cochalla.cochalla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.PostResponseDto;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("""
            SELECT new com.cochalla.cochalla.dto.PostResponseDto(
                p.postId, p.isPublic,
                s.title, s.content, s.createdAt,
                u.userId, u.nickname, u.profileImg,
                null, COUNT(allLikes.post.postId), CAST(SIZE(p.comments) AS int)
            )
            FROM Post p
            JOIN p.summary s
            JOIN p.user u
            LEFT JOIN p.likes l ON l.post.postId = p.postId
            LEFT JOIN p.likes allLikes
            WHERE p.postId = :postId
            GROUP BY p.postId, p.isPublic, s.title, s.content, s.createdAt, u.userId, u.nickname, u.profileImg
        """)
    Optional<PostResponseDto> findPostResponseDtoById(@Param("postId") Integer postId);

    Optional<Post> findByPostIdAndUser_userId(Integer postId, String userId);

    Boolean existsByPostIdAndUser_userId(Integer postId, String userId);
}
