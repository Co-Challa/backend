package com.cochalla.cochalla.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cochalla.cochalla.domain.Post;
import com.cochalla.cochalla.dto.PostSummaryDto;
import com.cochalla.cochalla.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public List<PostSummaryDto> getAllPostSummaries() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(post -> {
                    String userId = post.getUser() != null ? post.getUser().getUserId() : "";
                    String nickname = post.getUser() != null ? post.getUser().getNickname() : "";
                    String userImageUrl = post.getUser() != null ? getUserImageUrl(post.getUser().getProfileImg()) : "";

                    LocalDateTime createdAt = post.getSummary() != null ? post.getSummary().getCreatedAt() : null;
                    String title = post.getSummary() != null ? post.getSummary().getTitle() : "";
                    String content = post.getSummary() != null && post.getSummary().getContent() != null
                            ? post.getSummary().getContent()
                            : "";

                    // 본문 5줄 요약
                    String summary = Arrays.stream(content.split("\\r?\\n"))
                            .limit(5)
                            .collect(Collectors.joining("\n"));

                    // 좋아요/댓글 개수 안전하게 체크
                    long likesCount = post.getLikes() != null ? post.getLikes().size() : 0L;
                    long commentsCount = post.getComments() != null ? post.getComments().size() : 0L;

                    return PostSummaryDto.builder()
                            .postId(post.getPostId())
                            .title(title)
                            .userId(userId)
                            .nickname(nickname)
                            .userImageUrl(userImageUrl)
                            .createdAt(createdAt)
                            .summary(summary)
                            .likesCount(likesCount)
                            .commentsCount(commentsCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getUserImageUrl(Integer profileImgCode) {
        if (profileImgCode == null)
            return "/images/default_profile.png";

        switch (profileImgCode) {
            case 1:
                return "/images/profile1.png";
            case 2:
                return "/images/profile2.png";
            case 3:
                return "/images/profile3.png";
            default:
                return "/images/default_profile.png";
        }
    }

}
