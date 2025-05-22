package com.cochalla.cochalla.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(length = 20)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private Integer profileImg;

    @Column(nullable = false)
    private Integer resTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDate lastSummaryDate;

    @OneToMany(mappedBy = "user")
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Summary> summaries = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void markSummaryAsDone() {
        this.lastSummaryDate = LocalDate.now();
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImg(Integer profileImg) {
        this.profileImg = profileImg;
    }

    public void setResTime(Integer resTime) {
        this.resTime = resTime;
    }
}
