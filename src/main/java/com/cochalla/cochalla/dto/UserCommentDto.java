package com.cochalla.cochalla.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserCommentDto {
    private Integer postId;
    private String postAuthorNickname;
    private String postTitle;
    private Integer commentId;
    private String comment;
    private LocalDateTime createdAt;
}