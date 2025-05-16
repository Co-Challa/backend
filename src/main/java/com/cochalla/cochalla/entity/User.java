package com.cochalla.cochalla.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
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
}

