package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostPageDto;

public interface PostService {
    PostPageDto get(Integer postId);
    Boolean delete();
    Boolean togglePublic();
}
