package com.cochalla.cochalla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCommentDto {
    private Integer postId;
    private String nickname;
    private String postTitle;
    private Integer commentId;
    private String comment;
    private LocalDateTime createdAt;
}
