package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostPageDto;

public interface PostService {
    PostPageDto get(Integer postId);
    void delete(Integer postId);
    void setPublicState(Integer postId, Boolean isPublic);
}
