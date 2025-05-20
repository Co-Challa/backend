package com.cochalla.cochalla.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cochalla.cochalla.dto.*;
import com.cochalla.cochalla.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public UserInfoDto getUserInfo(@PathVariable("user_id") String userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/list/{user_id}")
    public List<UserPostDto> getUserPosts(
            @PathVariable("user_id") String userId,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getUserPosts(userId, offset, limit);
    }

    @GetMapping("/liked/{user_id}")
    public List<UserPostDto> getLikedPosts(
            @PathVariable("user_id") String userId,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getLikedPosts(userId, offset, limit);
    }

    @GetMapping("/comment/{user_id}")
    public List<UserCommentDto> getUserComments(
            @PathVariable("user_id") String userId,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getUserComments(userId, offset, limit);
    }

    @PostMapping("/update/{user_id}")
    public ResponseEntity<Void> updateUserInfo(
        @PathVariable("user_id") String userId,
        @RequestBody UserUpdateDto dto
    ) {
        userService.updateUser(userId, dto);
        return ResponseEntity.noContent().build();
    }
}
