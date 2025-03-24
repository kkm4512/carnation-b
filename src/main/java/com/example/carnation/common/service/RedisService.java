package com.example.carnation.common.service;

import com.example.carnation.common.service.constans.VerificationCodeStatus;
import com.example.carnation.security.JwtManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String REFRESH_KEY_PREFIX = "refreshToken::";
    private final String AUTH_KEY_PREFIX = "auth::";
    private final String BLACK_LIST_KEY_PREFIX = "blacklist::";

    private final String USER_ID_KEY_PREFIX = "userId:";
    private final String PHONE_KEY_PREFIX = "phone:";
    private final String EXPIRES_KEY_PREFIX = "expires:";
    private final JwtManager jwtManager;


    public void saveRefreshToken(Long userId, String refreshToken) {
        String redisKey = REFRESH_KEY_PREFIX + USER_ID_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(
                redisKey,
                refreshToken,
                7,
                TimeUnit.DAYS
        );
    }

    public String getRefreshToken(Long userId) {
        String redisKey = REFRESH_KEY_PREFIX + USER_ID_KEY_PREFIX + userId;
        return (String) redisTemplate.opsForValue().get(redisKey);
    }

    public void deleteRefreshToken(Long userId) {
        String redisKey = REFRESH_KEY_PREFIX + USER_ID_KEY_PREFIX + userId;
        redisTemplate.delete(redisKey);
    }

    public void saveVerificationCode(String phoneNumber, String verificationCode) {
        String redisKey = AUTH_KEY_PREFIX + PHONE_KEY_PREFIX + phoneNumber;
        String expireKey = AUTH_KEY_PREFIX + EXPIRES_KEY_PREFIX + phoneNumber;
        redisTemplate.opsForValue().set(
                redisKey,
                verificationCode,
                5,
                TimeUnit.MINUTES
        );

        redisTemplate.opsForValue().set(
                expireKey,
                "true",
                6,
                TimeUnit.MINUTES
        );
    }

    public String getVerificationCode(String phoneNumber) {
        String redisKey = AUTH_KEY_PREFIX + PHONE_KEY_PREFIX + phoneNumber;
        return (String) redisTemplate.opsForValue().get(redisKey);
    }

    public VerificationCodeStatus getVerificationCodeStatus(String phoneNumber) {
        String redisKey = AUTH_KEY_PREFIX + PHONE_KEY_PREFIX + phoneNumber;
        String expireKey = AUTH_KEY_PREFIX + EXPIRES_KEY_PREFIX + phoneNumber;

        if (redisTemplate.hasKey(redisKey)) {
            return VerificationCodeStatus.VALID;
        }
        if (redisTemplate.hasKey(expireKey)) {
            return VerificationCodeStatus.EXPIRED;
        }
        return VerificationCodeStatus.NOT_FOUND;
    }

    public void saveAccessTokenBlacklist(Long userId, String oldAccessToken) {
        // 1. JWT에서 만료 시간 추출
        Date expiration = jwtManager.toClaims(oldAccessToken).getExpiration();
        long now = System.currentTimeMillis();
        long ttl = expiration.getTime() - now;

        // 2. 유효 시간이 지나지 않았을 때만 블랙리스트 등록
        if (ttl > 0) {
            String blacklistKey = BLACK_LIST_KEY_PREFIX + USER_ID_KEY_PREFIX + userId;
            redisTemplate.opsForValue().set(blacklistKey, oldAccessToken, ttl, TimeUnit.MILLISECONDS);
        }
    }

    public String getBlackAccessToken(Long userId) {
        return (String) redisTemplate.opsForValue().get(BLACK_LIST_KEY_PREFIX + USER_ID_KEY_PREFIX + userId);
    }

}
