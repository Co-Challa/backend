package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostDto {

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

    // Like & Comment Count
    Integer totalLikeCnt;
    Integer totalCommentCnt;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("postId = ").append(postId)
            .append("\nTitle = ").append(title)
            .append("\nContent = ").append(content)
            .append("\nCreatedAt = ").append(createdAt)
            .append("\nUserId = ").append(userId)
            .append("\nNickname = ").append(nickname)
            .append("\nProfile = ").append(profileImg)
            .append("\nTotal Like Count = ").append(totalLikeCnt)
            .append("\nTotal Comments Count = ").append(totalCommentCnt).append("\n");
        
        return str.toString();
    }
}
