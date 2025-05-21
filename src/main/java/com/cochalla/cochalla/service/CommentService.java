package com.cochalla.cochalla.service;

import java.util.List;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.dto.CommentResponseDto;

public interface CommentService {
    List<CommentResponseDto> getPostCommentList(Integer postId, Integer offset, Integer limit);
    List<CommentResponseDto> getUserCommentList(String userId, Integer offset, Integer limit);
    Comment create(Integer postId, String userId, String comment);
    void delete(Integer commentId, String userId);
}
