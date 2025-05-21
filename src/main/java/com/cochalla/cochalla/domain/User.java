package com.cochalla.cochalla.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @Column(length = 20)
    private String userId;

    @Column(length = 20, nullable = false)
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

    public void markSummaryAsDone() {
        this.lastSummaryDate = LocalDate.now();
    }
}
