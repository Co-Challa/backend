package com.cochalla.cochalla.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.MainPostDto;
import com.cochalla.cochalla.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainPostServiceImpl implements MainPostService {
    private final PostRepository postRepository;

    // 무한 스크롤
    @Override
    public List<MainPostDto> getPostSummariesByPage(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        List<Post> posts = mainpostRepository.findAllByOrderByPostIdDesc(pageable);

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //조회
    @Override
    public List<MainPostDto> getAllPostSummaries() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 공통 DTO 변환 메서드
    private MainPostDto convertToDto(Post post) {
        String userId = post.getUser() != null ? post.getUser().getUserId() : "";
        String nickname = post.getUser() != null ? post.getUser().getNickname() : "";
        Integer profileImgCode = post.getUser() != null ? post.getUser().getProfileImg() : null;

        String title = post.getSummary() != null ? post.getSummary().getTitle() : "";
        String content = post.getSummary() != null ? post.getSummary().getContent() : "";
        LocalDateTime createdAt = post.getSummary() != null ? post.getSummary().getCreatedAt() : null;

        long likesCount = post.getLikes() != null ? post.getLikes().size() : 0L;
        long commentsCount = post.getComments() != null ? post.getComments().size() : 0L;

        return MainPostDto.builder()
                .postId(post.getPostId())
                .title(title)
                .content(content)
                .userId(userId)
                .nickname(nickname)
                .profileImgCode(profileImgCode)
                .createdAt(createdAt)
                .likesCount(likesCount)
                .commentsCount(commentsCount)
                .build();
    }


}
