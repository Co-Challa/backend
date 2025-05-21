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

import com.cochalla.cochalla.dto.CommentResponseDto;
import com.cochalla.cochalla.dto.CommentRequestDto;
import com.cochalla.cochalla.dto.CommentResponseDto;
import com.cochalla.cochalla.service.CommentServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CommentController {

    @Autowired
    CommentServiceImpl commentService;

    @GetMapping("/comment/{userId}")
    public ResponseEntity<?> getUserComments(
        @PathVariable String userId,
        @RequestParam Integer offset,
        @RequestParam Integer limit
    ) {
        try {
            List<CommentResponseDto> commentList = commentService.getUserCommentList(userId, offset, limit);

            return ResponseEntity.ok().body(commentList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/my-comments")
    public ResponseEntity<?> getmyComments(
        @RequestParam Integer offset,
        @RequestParam Integer limit,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            List<CommentResponseDto> commentList = commentService.getUserCommentList(userId, offset, limit);

            return ResponseEntity.ok().body(commentList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/comment/list")
    public ResponseEntity<?> getMethodName(
        @RequestParam Integer postId,
        @RequestParam Integer offset,
        @RequestParam Integer limit
    ) {
        try {
            List<CommentResponseDto> commentList = commentService.getPostCommentList(postId, offset, limit);

            return ResponseEntity.ok(commentList);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PostMapping("/comment/{postId}")
    public ResponseEntity<?> postComment(
        @PathVariable Integer postId, 
        @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            String userId = userDetails.getUsername();

            commentService.create(postId, userId, commentRequestDto.getComment());

            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("comment/{commentId}")
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
