package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.MainPostDto;


import java.util.List;

public interface MainPostService {
    List<MainPostDto> getAllPostSummaries();

    List<MainPostDto> getPostSummariesByPage(int offset, int limit);

        
}