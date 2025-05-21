package com.cochalla.cochalla.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Table(name = "answer")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer answerId;

    @OneToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(length = 500)
    private String answer;

    @Column
    private LocalDateTime createdAt;
}