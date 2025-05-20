package com.cochalla.cochalla.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLikeDto {
    private Integer postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer commentCnt;
    private Integer likeCnt;
    private Boolean isPublic;
    private String authorName;
    private Integer authorProfileImg;
}