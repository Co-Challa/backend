// src/main/java/com/cochalla/cochalla/service/UserService.java
package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.UserInfoDto;
import com.cochalla.cochalla.dto.UserPostDto;
import com.cochalla.cochalla.dto.UserCommentDto;
import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.repository.UserRepository;
import com.cochalla.cochalla.repository.UserPostRepository;
import com.cochalla.cochalla.repository.UserLikedRepository;
import com.cochalla.cochalla.repository.UserCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPostRepository userPostRepository;
    @Autowired
    private UserLikedRepository userLikedRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;

    public UserInfoDto getUserInfo(String userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        return new UserInfoDto(
            user.getUserId(),
            user.getNickname(),
            user.getProfileImg(),
            user.getResTime()
        );
    }

    public List<UserPostDto> getUserPosts(String userId, int offset, int limit) {
        int page = offset / limit;
        return userPostRepository
            .findByUserUserId(userId, PageRequest.of(page, limit))
            .stream()
            .map(p -> new UserPostDto(
                p.getPostId(),
                p.getSummary().getTitle(),
                p.getSummary().getContent(),
                p.getSummary().getCreatedAt(),
                p.getComments().size(),
                p.getLikes().size(),
                p.getIsPublic()
            ))
            .collect(Collectors.toList());
    }

    public List<UserPostDto> getLikedPosts(String userId, int offset, int limit) {
        int page = offset / limit;
        return userLikedRepository
            .findByUserUserId(userId, PageRequest.of(page, limit))
            .stream()
            .map(like -> {
                Post p = like.getPost();
                return new UserPostDto(
                    p.getPostId(),
                    p.getSummary().getTitle(),
                    p.getSummary().getContent(),
                    p.getSummary().getCreatedAt(),
                    p.getComments().size(),
                    p.getLikes().size(),
                    p.getIsPublic()
                );
            })
            .collect(Collectors.toList());
    }

    public List<UserCommentDto> getUserComments(String userId, int offset, int limit) {
        int page = offset / limit;
        return userCommentRepository
            .findByUserUserId(userId, PageRequest.of(page, limit))
            .stream()
            .map(c -> new UserCommentDto(
                c.getPost().getPostId(),
                c.getUser().getNickname(),
                c.getPost().getSummary().getTitle(),
                c.getPostCommentId(),
                c.getContent(),
                c.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
}