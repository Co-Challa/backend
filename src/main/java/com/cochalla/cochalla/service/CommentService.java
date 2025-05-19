package com.cochalla.cochalla.service;

import java.util.List;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.dto.CommentDto;

public interface CommentService {
    List<CommentDto> getUserCommentList(String userId);
    Comment create(Integer postId, String userId, String comment);
    void delete(Integer commentId, String userId);
}
