package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostSummaryDto;
import java.util.List;

public interface MainPostService {
    List<PostSummaryDto> getAllPostSummaries();
}
