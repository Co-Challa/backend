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

    @Lob
    private String answer;

    @Column
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // == 연관관게 메서드 ==
    public void setQuestion(Question question) {
        this.question = question;
        if (question.getAnswer() != this) {
            question.setAnswer(this);
        }
    }

    // == 생성 메서드 ==//
    public static Answer createAnswer(Question question, String answerText) {
        Answer answer = new Answer();
        answer.answer = answerText;
        answer.setQuestion(question); // 양방향 연관관계 자동 설정
        return answer;
    }

}
