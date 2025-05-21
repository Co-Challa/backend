package com.cochalla.cochalla.service;

import java.util.List;

import com.cochalla.cochalla.dto.CommentResponseDto;

public interface CommentService {
    List<CommentResponseDto> getPostCommentList(Integer postId, Integer offset, Integer limit);
    List<CommentResponseDto> getUserCommentList(String userId, Integer offset, Integer limit);
    Long create(Integer postId, String userId, String comment);
    Long delete(Integer commentId, String userId);
}
