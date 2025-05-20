package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponseDto {

    // Post
    Integer postId;
    Boolean isPublic;

    // Summary
    String title;
    String content;
    LocalDateTime createdAt;

    // User
    String userId;
    String nickname;
    Integer profileImg;
    
    // Like
    Boolean isLike;
    Long likeCount;

    // Comment
    Integer commentCount;
}
