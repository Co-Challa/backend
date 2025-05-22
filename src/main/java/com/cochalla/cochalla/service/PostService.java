package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostResponseDto;

public interface PostService {
    PostResponseDto get(Integer postId, String userId);
    void delete(Integer postId, String userId);
    void setPublicState(Integer postId, String userId, Boolean isPublic);
}
