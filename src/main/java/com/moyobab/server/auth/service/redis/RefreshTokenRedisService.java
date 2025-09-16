package com.moyobab.server.auth.service.redis;

import com.moyobab.server.global.util.HmacUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${hmac.secret}")
    private String hmacSecret;
    private static final String PREFIX = "refreshToken:";

    private String key(Long userId) {
        return PREFIX + userId;
    }

    private String hash(String token) {
        return HmacUtils.hmacSha256(hmacSecret, token);
    }

    public void save(Long userId, String rawToken, long ttlMillis) {
        String hashedToken = hash(rawToken);
        if (ttlMillis <= 0) {
            redisTemplate.delete(key(userId));
            return;
        }
        redisTemplate.opsForValue().set(key(userId), hashedToken, Duration.ofMillis(ttlMillis));
    }

    public boolean isSame(Long userId, String rawToken) {
        String storedHash = redisTemplate.opsForValue().get(key(userId));
        if (storedHash == null) return false;
        String requestHash = hash(rawToken);
        return HmacUtils.isEqual(storedHash, requestHash);
    }

    public void delete(Long userId) {
        redisTemplate.delete(key(userId));
    }
}
