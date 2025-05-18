package com.cochalla.cochalla.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
public class PostController {
    
    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable Integer postId) {
        return "getPost";
    }
    
    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Integer postId) {
        return "deletePost";
    }

    @PatchMapping("/post/{postId}")
    public String patchPost(@PathVariable Integer postId, @RequestBody Boolean isPublic) {
        return "pathcPost";
    }

    @PostMapping("/post/{postId}/comment")
    public String postComment(@PathVariable Integer postId, @RequestBody String comment) {
        return "postComment";
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public String deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId) {
        return "deleteComment";
    }

    @PostMapping("/post/{postId}/like")
    public String postLike(@PathVariable Integer postId, @RequestBody Boolean isLike) {
        return "postLike";
    }
    
}
