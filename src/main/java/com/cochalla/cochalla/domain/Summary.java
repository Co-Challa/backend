package com.cochalla.cochalla.domain;

import com.cochalla.cochalla.dto.GptSummaryResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "summary")
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer summaryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "summary")
    @JsonIgnore
    @ToString.Exclude
    private Post post;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Summary(User user, Chat chat, String title, String content, LocalDateTime createdAt) {
        this.user = user;
        this.chat = chat;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Summary of(User user, Chat chat, String title, String content) {
        if (user == null || chat == null) {
            throw new IllegalArgumentException("Summary 생성 시 user와 chat은 null일 수 없습니다.");
        }

        return Summary.builder()
                .user(user)
                .chat(chat)
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public void updateFromResponse(GptSummaryResponseDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }
}
