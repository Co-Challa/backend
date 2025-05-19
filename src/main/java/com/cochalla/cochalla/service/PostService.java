package com.cochalla.cochalla.service;

import com.cochalla.cochalla.dto.PostSummaryDto;
import java.util.List;

public interface PostService {
    List<PostSummaryDto> getAllPostSummaries();
}
