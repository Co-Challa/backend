package com.cochalla.cochalla.controller;

import org.springframework.web.bind.annotation.RestController;

import com.cochalla.cochalla.dto.LoginRequestDto;
import com.cochalla.cochalla.dto.SignupRequestDto;
import com.cochalla.cochalla.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService user_service;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto requestDto) {
        user_service.signup(requestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        String token = user_service.login(requestDto);
        return ResponseEntity.ok()
        .header("Authorization", "Bearer " + token)
        .body("로그인 성공");
    }
}
