package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter @Setter
@AllArgsConstructor
@Builder
public class PostDto {
    //게시글 
    Integer postId;
    Boolean isPublic;

    //요약글 
    String title;
    String cotent;
    LocalDateTime createdAt;

    //User
    String userId;
    String nickname;
    Integer profileImg;

    //좋아요,댓글
    Integer totalLikecnt;
    Integer totalCommentCnt;

}

