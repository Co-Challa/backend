package com.cochalla.cochalla.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class PostDto {
    private Integer postId;
    private String userId;
    private Integer summaryId;
    private Boolean isPublic;
}
