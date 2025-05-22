package com.cochalla.cochalla.controller;

import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.repository.SummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryRepository summaryRepository;

    // summary 저장 확인용 api
    @GetMapping("/result")
    public ResponseEntity<?> getLatestSummary(@RequestParam String userId) {
        Summary summary = summaryRepository.findTopByUser_UserIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new RuntimeException("요약 없음"));
        return ResponseEntity.ok(Map.of(
                "title", summary.getTitle(),
                "content", summary.getContent()
        ));
    }
}
