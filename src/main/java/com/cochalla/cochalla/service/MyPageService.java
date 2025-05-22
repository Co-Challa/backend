package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.*;
import com.cochalla.cochalla.domain.User;
import com.cochalla.cochalla.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

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
                user.getResTime());
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
                        p.getIsPublic(),
                        userLikedRepository.existsByUserUserIdAndPostPostId(userId,
                                p.getPostId())))
                .collect(Collectors.toList());
    }

    public List<UserLikeDto> getLikedPosts(String userId, int offset, int limit) {
        int page = offset / limit;
        return userLikedRepository
                .findUserLikedPosts(userId, PageRequest.of(page, limit))
                .getContent();
    }

    public List<UserCommentDto> getUserComments(String userId, int offset, int limit) {
        int page = offset / limit;
        return userCommentRepository
                .findByUserUserId(userId, PageRequest.of(page, limit))
                .stream()
                .map(c -> new UserCommentDto(
                        c.getPost().getPostId(),
                        c.getPost().getUser().getNickname(),
                        c.getPost().getSummary().getTitle(),
                        c.getPostCommentId(),
                        c.getContent(),
                        c.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public void updateUser(String userId, UserUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.setNickname(dto.getNickname());
        user.setProfileImg(dto.getProfileImg());
        user.setResTime(dto.getResTime());
        userRepository.save(user);
    }
}