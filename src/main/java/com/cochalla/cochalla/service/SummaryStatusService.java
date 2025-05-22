package com.cochalla.cochalla.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SummaryStatusService {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "summary:status:";

    public void setPending(String userId) {
        setStatus(userId, "PENDING", Duration.ofMinutes(10));
    }

    public void setSuccess(String userId) {
        setStatus(userId, "SUCCESS", Duration.ofMinutes(60));
    }

    public void setFailed(String userId) {
        setStatus(userId, "FAILED", Duration.ofMinutes(30));
    }

    private void setStatus(String userId, String status, Duration ttl) {
        redisTemplate.opsForValue().set(PREFIX + userId, status, ttl);
    }
}