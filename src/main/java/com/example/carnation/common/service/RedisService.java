package com.example.carnation.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String REFRESH_KEY_PREFIX = "refreshToken::";
    private final String USER_ID_KEY_PREFIX = "userId:";


    public void saveRefreshToken(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                REFRESH_KEY_PREFIX + USER_ID_KEY_PREFIX + userId,
                refreshToken,
                7,
                TimeUnit.DAYS
        );
    }

    public String getRefreshToken(Long userId) {
        return (String) redisTemplate.opsForValue().get(
                REFRESH_KEY_PREFIX +
                        USER_ID_KEY_PREFIX +
                        userId
        );
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(
                REFRESH_KEY_PREFIX +
                        USER_ID_KEY_PREFIX +
                        userId
        );
    }
}
