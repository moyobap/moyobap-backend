package com.moyobab.server.auth.service.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "refreshToken:";

    public void save(Long userId, String refreshToken, long ttlMillis) {
        String key = PREFIX + userId;

        if (ttlMillis <= 0) {
            redisTemplate.delete(key);
            return;
        }

        redisTemplate.opsForValue().set(
                key,
                refreshToken,
                Duration.ofMillis(ttlMillis)
        );
    }

    public String get(Long userId) {
        return redisTemplate.opsForValue().get(PREFIX + userId);
    }

    public void delete(Long userId) {
        redisTemplate.delete(PREFIX + userId);
    }

    public boolean isSame(Long userId, String refreshToken) {
        String saved = get(userId);
        return saved != null && saved.equals(refreshToken);
    }
}
