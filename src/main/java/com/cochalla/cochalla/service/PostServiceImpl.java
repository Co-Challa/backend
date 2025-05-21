package com.cochalla.cochalla.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.LikeId;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.PostResponseDto;
import com.cochalla.cochalla.repository.CommentRepository;
import com.cochalla.cochalla.repository.LikeRepository;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;
    
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    LikeRepository likeRepository;

    @Override
    public PostResponseDto get(Integer postId, String userId) {

        PostResponseDto postResponseDto = postRepository.findPostResponseDtoById(postId)
                                    .orElseThrow(() -> new NoSuchElementException(postId + "번 게시물을 찾을 수 없습니다."));

        if (userId.isEmpty()){
            postResponseDto.setIsLike(false);
        }
        else {
            LikeId likeId = new LikeId();
            likeId.setPostId(postId);
            likeId.setUserId(userId);

            Boolean isLike = likeRepository.existsById(likeId);

            postResponseDto.setIsLike(isLike);
        }

        return postResponseDto;
    }

    @Override
    public void delete(Integer postId, String userId) {
        if (!postRepository.existsByPostIdAndUser_userId(postId, userId))
            throw new NoSuchElementException(postId + "번 게시물의 삭제 권한이 없거나 존재하지 않습니다.");

        postRepository.deleteById(postId);
    }

    @Override
    @Transactional
    public void setPublicState(Integer postId, String userId, Boolean isPublic) {
        Optional<Post> optPost = postRepository.findByPostIdAndUser_userId(postId, userId);
        Post post = optPost.orElseThrow(() -> new NoSuchElementException(postId + "번 게시물의 수정 권한이 없거나 게시물이 존재하지 않습니다."));

        post.setIsPublic(isPublic);

        postRepository.save(post);
    }
    
}
