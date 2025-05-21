package com.cochalla.cochalla.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cochalla.cochalla.dto.CommentDto;
import com.cochalla.cochalla.service.CommentServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class CommentController {

    @Autowired
    CommentServiceImpl commentService;

    @GetMapping("/comment/{userId}")
    public ResponseEntity<?> getUserComments(@PathVariable String userId) {
        try {
            List<CommentDto> commentList = commentService.getUserCommentList(userId);

            return ResponseEntity.ok().body(commentList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/my-comments")
    public ResponseEntity<?> getmyComments(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            String userId = userDetails.getUsername();

            List<CommentDto> commentList = commentService.getUserCommentList(userId);

            return ResponseEntity.ok().body(commentList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/comment/{postId}")
    @ResponseBody
    public ResponseEntity<?> postComment(
        @PathVariable Integer postId, 
        @RequestBody String comment,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            commentService.create(postId, userId, comment);

            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("comment/{commentId}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(
        @PathVariable Integer commentId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            commentService.delete(commentId, userId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
