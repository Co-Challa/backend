package com.cochalla.cochalla.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "`like`")
@IdClass(LikeId.class)
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

