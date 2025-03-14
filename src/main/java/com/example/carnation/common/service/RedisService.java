package com.example.carnation.common.service;

import com.example.carnation.common.service.constans.VerificationCodeStatus;
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
    private final String AUTH_KEY_PREFIX = "auth::";
    private final String PHONE_KEY_PREFIX = "phone:";
    private final String EXPIRES_KEY_PREFIX = "expires:";


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

}
