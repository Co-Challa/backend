package com.cochalla.cochalla.domain;

import java.io.Serializable;
import java.util.Objects;

public class LikeId implements Serializable {
    private Integer post;
    private String user;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LikeId))
            return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(post, likeId.post) && Objects.equals(user, likeId.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post, user);
    }
}