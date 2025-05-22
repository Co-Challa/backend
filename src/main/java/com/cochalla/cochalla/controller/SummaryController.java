package com.cochalla.cochalla.controller;

import com.cochalla.cochalla.domain.Chat;
import com.cochalla.cochalla.domain.Summary;
import com.cochalla.cochalla.repository.ChatRepository;
import com.cochalla.cochalla.repository.SummaryRepository;
import com.cochalla.cochalla.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryRepository summaryRepository;
    private final SummaryService summaryService;
    private final ChatRepository chatRepository;

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

    // 마이페이지에서 실패 Summary에 대해 재요약 요청
    @PostMapping("/retry")
    public ResponseEntity<?> retrySummary(@RequestParam Integer summaryId) {
        Summary summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new RuntimeException("Summary not found"));

        // 성공 Summary라면 재요약 불가
        if (summary.getTitle() != null && summary.getContent() != null) {
            return ResponseEntity.badRequest().body("이미 성공한 요약입니다.");
        }

        try {
            summaryService.retrySummary(summary);
            return ResponseEntity.ok("재요약 성공!"); // 필요시 summary 데이터 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("재요약에 실패했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/test")
    public ResponseEntity<?> testSummary(@RequestParam Integer chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        try {
            summaryService.testGenerateSummary(chat);
            return ResponseEntity.ok("요약 테스트 성공!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("요약 테스트 실패: " + e.getMessage());
        }
    }
}
