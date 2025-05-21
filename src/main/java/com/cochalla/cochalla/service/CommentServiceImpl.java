package com.cochalla.cochalla.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cochalla.cochalla.domain.Comment;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.dto.CommentResponseDto;
import com.cochalla.cochalla.repository.CommentRepository;
import com.cochalla.cochalla.repository.PostRepository;
import com.cochalla.cochalla.repository.UserRepository;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<CommentResponseDto> getPostCommentList(Integer postId, Integer offset, Integer limit) {
        if (!postRepository.existsById(postId))
            throw new NoSuchElementException(postId + "번 게시물이 존재하지 않습니다.");

        if (offset == null || limit == null) 
            throw new NoSuchElementException("Pagination 처리 불가");

        Integer page = offset / limit;
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<CommentResponseDto> commentPage = commentRepository.findCommentsByPostId(postId, pageable);

        return commentPage.toList();
    }

    @Override
    public List<CommentResponseDto> getUserCommentList(String userId, Integer offset, Integer limit) {

        if (!userRepository.existsById(userId))
            throw new NoSuchElementException(userId + " 사용자가 존재하지 않습니다.");

        if (offset == null || limit == null) 
            throw new NoSuchElementException("Pagination 처리 불가");

        Integer page = offset / limit;
        Sort sort = Sort.by(Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, limit, sort);

        Page<CommentResponseDto> commentPage = commentRepository.findCommentsByUserId(userId, pageable);
        
        return commentPage.toList();
    }
    
    @Override
    @Transactional
    public Long create(Integer postId, String userId, String content) {
        if (!postRepository.existsById(postId))
            throw new NoSuchElementException(postId + "번 게시물이 존재하지 않습니다.");

        if (!userRepository.existsById(userId))
            throw new NoSuchElementException(userId + " 사용자가 존재하지 않습니다.");

        Post refPost = postRepository.getReferenceById(postId);
        User refUser = userRepository.getReferenceById(userId);

        Comment newComment = new Comment();
        newComment.setPost(refPost);
        newComment.setUser(refUser);
        newComment.setContent(content);
        newComment.setCreatedAt(LocalDateTime.now());

        commentRepository.save(newComment);

        return commentRepository.countByPost_postId(postId);
    }

    @Override
    public Long delete(Integer commentId, String userId) {
        if (commentId == null)
            throw new NoSuchElementException("commentId is NULL");

        if (userId.isEmpty())
            throw new NoSuchElementException("userId is EMPTY");

        Integer postId = commentRepository.findPostIdByCommentIdAndUserId(commentId, userId);
        if (postId == null)
            throw new NoSuchElementException(userId + "님이 조회한 " + commentId + "번 댓글이 조회되지 않습니다.");

        commentRepository.deleteById(commentId);

        return commentRepository.countByPost_postId(postId);
    }
    
}
