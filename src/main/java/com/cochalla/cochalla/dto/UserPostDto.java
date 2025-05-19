package com.cochalla.cochalla.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserPostDto {
    private Integer postId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Integer commentCnt;
    private Integer likeCnt;
    private Boolean isPublic;
     private Boolean liked;
}