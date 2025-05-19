package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostPageDto;

public interface PostService {
    PostPageDto get(Integer postId);
    void delete(Integer postId, String userId);
    void setPublicState(Integer postId, String userId, Boolean isPublic);
}
