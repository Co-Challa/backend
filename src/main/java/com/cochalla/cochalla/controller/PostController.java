package com.cochalla.cochalla.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cochalla.cochalla.dto.PostPageDto;
import com.cochalla.cochalla.service.CommentServiceImpl;
import com.cochalla.cochalla.service.PostServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    CommentServiceImpl commentService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostPageDto> getPost(@PathVariable Integer postId) {
        PostPageDto response = null;
        try {
            response = postService.get(postId);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<?> deletePost(
        @PathVariable Integer postId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            postService.delete(postId, userId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/post/{postId}")
    public ResponseEntity<?> patchPost(
        @PathVariable Integer postId, 
        @RequestBody Boolean isPublic,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            postService.setPublicState(postId, userId, isPublic);

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/like/{postId}")
    @ResponseBody
    public String postLike(@PathVariable Integer postId, @RequestBody Boolean isLike) {
        return "postLike";
    }
    
}
