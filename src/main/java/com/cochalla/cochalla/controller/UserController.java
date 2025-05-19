package com.cochalla.cochalla.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cochalla.cochalla.dto.UserCommentDto;
import com.cochalla.cochalla.dto.UserInfoDto;
import com.cochalla.cochalla.dto.UserPostDto;
import com.cochalla.cochalla.service.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{user_id}")
    public UserInfoDto getUserInfo(@PathVariable String user_id) {
        return userService.getUserInfo(user_id);
    }

    @GetMapping("/list/{user_id}")
    public List<UserPostDto> getUserPosts(
            @PathVariable String user_id,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getUserPosts(user_id, offset, limit);
    }

    @GetMapping("/liked/{user_id}")
    public List<UserPostDto> getLikedPosts(
            @PathVariable String user_id,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getLikedPosts(user_id, offset, limit);
    }

    @GetMapping("/comment/{user_id}")
    public List<UserCommentDto> getUserComments(
            @PathVariable String user_id,
            @RequestParam int offset,
            @RequestParam int limit) {
        return userService.getUserComments(user_id, offset, limit);
    }
}
