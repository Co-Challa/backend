package com.cochalla.cochalla.dto;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PostPageDto {
    PostDto post;
    List<CommentDto> comments;

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer()
            .append("[Post Page]\n")
            .append(post.toString())
            .append("[Comment]\n");

        comments.forEach((comment)->{
            str.append(comment.toString());
        });

        return str.toString();
    }

}
