package com.cochalla.cochalla.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.Like;
import com.cochalla.cochalla.domain.LikeId;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.repository.LikeRepository;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.UserRepository;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LikeRepository likeRepository;

    @Override
    public void setLikeState(Integer postId, String userId, Boolean likeState) {
        if (!postRepository.existsById(postId))
            throw new NoSuchElementException(postId + "번 게시물이 존재하지 않습니다.");

        if (!userRepository.existsById(userId))
            throw new NoSuchElementException(userId + " 사용자가 존재하지 않습니다.");

        LikeId likeId = new LikeId();
        likeId.setPostId(postId);
        likeId.setUserId(userId);

        Optional<Like> like = likeRepository.findById(likeId);
        if (likeState && !like.isPresent()) { // 추천 하고자 하고 DB에 추천되어 있지 않으면, 추가
            Post postRef = postRepository.getReferenceById(postId);
            User userRef = userRepository.getReferenceById(userId);

            Like newLike = new Like();
            newLike.setId(likeId);
            newLike.setPost(postRef);
            newLike.setUser(userRef);

            likeRepository.save(newLike);
        }
        else if (!likeState && like.isPresent()) { // 추천 취소하고자 하고 DB에 추천되어 있으면, 제거
            likeRepository.deleteById(likeId);
        }
    }
    
}
