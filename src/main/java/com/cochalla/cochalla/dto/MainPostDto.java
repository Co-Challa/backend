package com.cochalla.cochalla.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MainPostDto {
    private Integer postId;
    private String title;
    private String summary;

    private String userId;
    private String nickname;
    private String userImageUrl;

    private LocalDateTime createdAt; 
    
    private Long likesCount;
    private Long commentsCount;

}

