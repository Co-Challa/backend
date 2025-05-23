package com.cochalla.cochalla.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cochalla.cochalla.dto.MainPostDto;
import com.cochalla.cochalla.security.CustomUserDetail;
import com.cochalla.cochalla.service.MainPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class MainPostController {
    private final MainPostService mainpostService;

    private static final int PAGE_SIZE = 10; // 페이지당 게시글 수

    @GetMapping("/list")
    public List<MainPostDto> getPostsByPage(@RequestParam(defaultValue = "0") int page, 
    @AuthenticationPrincipal CustomUserDetail userDetail
    ) {
        int offset = page * PAGE_SIZE; 
        String currentUserId = (userDetail != null) ? userDetail.getUsername() : null;
        return mainpostService.getPostSummariesByPage(offset, PAGE_SIZE,currentUserId);
        
    }


    
}
  
    


