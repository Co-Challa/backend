package com.cochalla.cochalla.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "summary")
public class Summary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer summaryId;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Column(length = 255)
    private String title;

    @Column(length = 255)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Integer retryCount;
}
