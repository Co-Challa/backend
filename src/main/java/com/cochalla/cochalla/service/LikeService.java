package com.cochalla.cochalla.service;

public interface LikeService {
    void setLikeState(Integer postId, String userId, Boolean likeState);
}
