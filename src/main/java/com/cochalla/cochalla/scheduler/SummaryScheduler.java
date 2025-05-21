package com.cochalla.cochalla.scheduler;

import com.cochalla.cochalla.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummaryScheduler {

    private final SummaryService summaryService;

    /*
    * <<NOTICE>> - @Scheduled
    * 매시 정각 실행
    * 테스트용 예: 매분마다 실행 (0 * * * * *)
    * */
    @Scheduled(cron = "0 0 * * * *")
    public void runHourlySummaryJob() {
        int currentHour = LocalTime.now().getHour();
        log.info("[스케줄러] {}시 요약 실행 시작", currentHour);
        summaryService.generateSummariesForHour(currentHour);
    }
}
