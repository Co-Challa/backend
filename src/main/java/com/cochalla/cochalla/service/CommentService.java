package com.cochalla.cochalla.service;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.dto.CommentResponseDto;

public interface CommentService {
    CommentResponseDto getPostCommentList(Integer postId, Integer page, Integer size);
    CommentResponseDto getUserCommentList(String userId, Integer page, Integer size);
    Comment create(Integer postId, String userId, String comment);
    void delete(Integer commentId, String userId);
}
