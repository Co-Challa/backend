package com.cochalla.cochalla.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    List<CommentDto> content;
    Boolean isLast;
    Integer totalPages;
    Long totalElements;
    Integer size;
    Integer page;
}
