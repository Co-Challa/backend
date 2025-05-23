package com.cochalla.cochalla.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToOne(mappedBy = "chat")
    @JsonIgnore
    @ToString.Exclude
    private Summary summary;

    private LocalDateTime createdAt;

    // == 자동 생성 시간 설정 == //
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // == 연관관계 메서드 ==//
    public void setUser(User user) {
        this.user = user;
    }

    // == 생성 메서드 == //
    public static Chat createChat(User user) {
        Chat chat = new Chat();
        chat.setUser(user);
        return chat;
    }
}