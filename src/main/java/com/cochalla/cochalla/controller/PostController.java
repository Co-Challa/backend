package com.cochalla.cochalla.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cochalla.cochalla.dto.PostResponseDto;
import com.cochalla.cochalla.dto.PostRequestDto;
import com.cochalla.cochalla.service.LikeServiceImpl;
import com.cochalla.cochalla.service.PostServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PostController {

    @Autowired
    PostServiceImpl postService;

    @Autowired
    LikeServiceImpl likeService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponseDto> getPost(
        @PathVariable Integer postId,
        @AuthenticationPrincipal UserDetails userDetails        
    ) {
        PostResponseDto response = null;
        try {
            String userId = "";
            if (userDetails != null)
                userId = userDetails.getUsername();

            response = postService.get(postId, userId);

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
        @RequestBody PostRequestDto postRequestDto, 
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            postService.setPublicState(postId, userId, postRequestDto.getIsPublic());

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/like/{postId}")
    @ResponseBody
    public ResponseEntity<?> postLike(
        @PathVariable Integer postId,
        @RequestBody PostRequestDto postRequestDto,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            likeService.setLikeState(postId, userId, postRequestDto.getIsLike());

            Long totalLikeCount = likeService.getTotalLikeCount(postId);

            return ResponseEntity.ok(totalLikeCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
