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
import com.cochalla.cochalla.dto.CommentDto;
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
    public List<CommentDto> getUserCommentList(String userId) {

        if (!userRepository.existsById(userId))
            throw new NoSuchElementException(userId + " 사용자가 존재하지 않습니다.");

        Sort sort = Sort.by(Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(0, 5, sort);

        Page<CommentDto> comments = commentRepository.findCommentsByUserId(userId, pageable);

        return comments.toList();
    }
    
    @Override
    @Transactional
    public Comment create(Integer postId, String userId, String content) {
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

        return commentRepository.save(newComment);
    }

    @Override
    public void delete(Integer commentId, String userId) {
        if (!commentRepository.existsByPostCommentIdAndUser_userId(commentId, userId))
            throw new NoSuchElementException(commentId + "번 댓글의 삭제 권한이 없거나 존재하지 않습니다.");

        commentRepository.deleteById(commentId);
    }
    
}
