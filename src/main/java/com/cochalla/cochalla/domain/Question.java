package com.cochalla.cochalla.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(length = 1500, nullable = false)
    private String question;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Answer answer;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;

        if (answer.getQuestion() != this) {
            answer.setQuestion(this);
        }
    }

    // == 생성 메서드 ==//
    public static Question createQuestion(Chat chat, String questionText) {
        Question question = new Question();
        question.chat = chat;
        question.question = questionText;
        return question;
    }
}
