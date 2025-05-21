package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentDto {

    // Comment
    Integer commentId;
    String content;
    LocalDateTime createdAt;
    
    // User
    String userId;
    String nickname;
    Integer profileImg;

    @Override
    public String toString() {
        return "\tCommentId = " + commentId
            + "\n\tUserId = " + userId
            + "\n\tNickname = " + nickname
            + "\n\tProfile = " + profileImg
            + "\n\tContent = " + content
            + "\n\tCreated = " + createdAt;
    }

    
}
