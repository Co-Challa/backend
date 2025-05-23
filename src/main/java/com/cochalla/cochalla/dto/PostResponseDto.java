package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class PostResponseDto {

    // Post
    Integer postId;
    Boolean isPublic;

    // Summary
    Integer chatId;
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
