package com.cochalla.cochalla.controller;

import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.cochalla.cochalla.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryRepository summaryRepository;
    private final SummaryService summaryService;

    @PostMapping("/retry")
    public ResponseEntity<?> retrySummary(@RequestParam Integer summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new RuntimeException("Summary not found"));

        if (summary.getTitle() != null && summary.getContent() != null) {
            return ResponseEntity.badRequest().body("이미 성공한 요약입니다.");
        }

        try {
            summaryService.retrySummary(summary);
            return ResponseEntity.ok("재요약에 성공했습니다!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("재요약에 실패했습니다: " + e.getMessage());
        }
    }
}
