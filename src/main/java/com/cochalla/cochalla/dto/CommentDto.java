package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDto {

    // Comment
    Integer commentId;
    String content;
    LocalDateTime createdAt;
    
    // User
    String userId;
    String nickname;
    Integer profileImg;
}
