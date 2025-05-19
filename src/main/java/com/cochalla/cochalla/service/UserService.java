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

    /**
     * 사용자 기본 정보 조회
     */
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

    /**
     * 내 게시글 리스트 조회 (사용자가 해당 글에 좋아요 눌렀는지도 포함)
     */
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
                userLikedRepository.existsByUserUserIdAndPostPostId(userId, p.getPostId())
            ))
            .collect(Collectors.toList());
    }

    /**
     * 관심(좋아요) 게시글 리스트 조회
     */
    public List<UserPostDto> getLikedPosts(String userId, int offset, int limit) {
        int page = offset / limit;
        return userLikedRepository
            .findByUserUserId(userId, PageRequest.of(page, limit))
            .stream()
            .map(likeEntry -> {
                Post p = likeEntry.getPost();
                return new UserPostDto(
                    p.getPostId(),
                    p.getSummary().getTitle(),
                    p.getSummary().getContent(),
                    p.getSummary().getCreatedAt(),
                    p.getComments().size(),
                    p.getLikes().size(),
                    p.getIsPublic(),
                    true // 좋아요한 목록이므로 항상 true
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * 내 댓글 리스트 조회
     */
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
                c.getCreatedAt()
            ))
            .collect(Collectors.toList());
    }
}
