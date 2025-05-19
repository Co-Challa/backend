package com.cochalla.cochalla.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cochalla.cochalla.dto.PostSummaryDto;
import com.cochalla.cochalla.service.PostService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public List<PostSummaryDto> getAllposts(){
        return postService.getAllPostSummaries();
    }

    
    
    
}
