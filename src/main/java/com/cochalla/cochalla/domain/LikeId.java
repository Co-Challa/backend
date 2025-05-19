package com.cochalla.cochalla.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class LikeId implements Serializable {
    private Integer postId;

    @Column(name = "user_id", length = 20)
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LikeId))
            return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(postId, likeId.postId) && Objects.equals(userId, likeId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, userId);
    }
}
