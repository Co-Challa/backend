package com.cochalla.cochalla.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cochalla.cochalla.dto.*;
import com.cochalla.cochalla.security.CustomUserDetail;
import com.cochalla.cochalla.service.MyPageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/user")
public class MyPageController {
    @Autowired
    private MyPageService myPageService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getUserInfo(
        @AuthenticationPrincipal CustomUserDetail userDetails
    ) {
        String userId = userDetails.getUsername();
        UserInfoDto dto = myPageService.getUserInfo(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<UserPostDto>> getUserPosts(
        @AuthenticationPrincipal CustomUserDetail userDetails,
        @RequestParam int offset,
        @RequestParam int limit
    ) {
        String userId = userDetails.getUsername();
        List<UserPostDto> list = myPageService.getUserPosts(userId, offset, limit);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<UserLikeDto> > getLikedPosts(
        @AuthenticationPrincipal CustomUserDetail userDetails,
        @RequestParam int offset,
        @RequestParam int limit
    ) {
        String userId = userDetails.getUsername();
        List<UserLikeDto> list = myPageService.getLikedPosts(userId, offset, limit);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/comments")
    public ResponseEntity<List<UserCommentDto>> getUserComments(
        @AuthenticationPrincipal CustomUserDetail userDetails,
        @RequestParam int offset,
        @RequestParam int limit
    ) {
        String userId = userDetails.getUsername();
        List<UserCommentDto> list = myPageService.getUserComments(userId, offset, limit);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateUserInfo(
        @AuthenticationPrincipal CustomUserDetail userDetails,
        @RequestBody UserUpdateDto dto
    ) {
        String userId = userDetails.getUsername();
        myPageService.updateUser(userId, dto);
        return ResponseEntity.noContent().build();
    }
}
