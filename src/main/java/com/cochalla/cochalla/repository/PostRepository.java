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
                    (
                        CASE WHEN :currentUserId IS NOT NULL AND EXISTS (
                            SELECT lk FROM Like lk
                            WHERE lk.post.postId = p.postId AND lk.user.userId = :currentUserId
                        ) THEN TRUE ELSE FALSE END
                    ),
                    (SELECT COUNT(l) FROM Like l WHERE l.post.postId = p.postId),
                    SIZE(p.comments)
                )
                FROM Post p
                JOIN p.summary s
                JOIN p.user u
                WHERE p.postId = :postId
            """)
    Optional<PostResponseDto> findPostResponseDto(@Param("postId") Integer postId,
            @Param("currentUserId") String currentUserId);

    Optional<Post> findByPostIdAndUser_userId(Integer postId, String userId);

    Boolean existsByPostIdAndUser_userId(Integer postId, String userId);
}
