package com.cochalla.cochalla.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.CommentDto;
import com.cochalla.cochalla.dto.PostDto;
import com.cochalla.cochalla.dto.PostPageDto;
import com.cochalla.cochalla.repository.CommentRepository;
import com.cochalla.cochalla.repository.PostRepository;

import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public PostPageDto get(Integer postId) {
        Optional<PostDto> optPost = postRepository.findPostById(postId);
        PostDto postDto = optPost.orElseThrow(() -> new NoSuchElementException(postId + "번 게시물을 찾을 수 없습니다."));

        Sort sort = Sort.by(Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 1, sort);        
        
        Page<CommentDto> comments = commentRepository.findCommentsByPostId(postDto.getPostId(), pageable);

        return new PostPageDto(postDto, comments.toList());
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
