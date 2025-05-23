package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.MainPostDto;


import java.util.List;

public interface MainPostService {
    List<MainPostDto> getAllPostSummaries(String currentUserId);

    List<MainPostDto> getPostSummariesByPage(int offset, int limit,String currentUserId);

        
}

