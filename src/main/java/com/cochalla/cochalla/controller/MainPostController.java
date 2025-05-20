package com.cochalla.cochalla.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cochalla.cochalla.dto.MainPostDto;
import com.cochalla.cochalla.service.MainPostService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class MainPostController {
    private final MainPostService mainpostService;

    @GetMapping("/list")
    public List<MainPostDto> getAllposts(){
        return mainpostService.getAllPostSummaries();
    }

    
    
    
}
