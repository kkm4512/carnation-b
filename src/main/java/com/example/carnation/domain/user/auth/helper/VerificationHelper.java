package com.example.carnation.domain.user.auth.helper;

import com.example.carnation.common.exception.AuthException;
import com.example.carnation.common.exception.UserException;
import com.example.carnation.common.service.RedisService;
import com.example.carnation.common.service.constans.VerificationCodeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.example.carnation.common.response.enums.AuthApiResponseEnum.*;
import static com.example.carnation.common.response.enums.UserApiResponseEnum.INVALID_CREDENTIALS;

@Component
@RequiredArgsConstructor
public class VerificationHelper {
    private final RedisService redisService;
    private final PasswordEncoder pe;

    public void verifyPassword(final String rawPassword, final String encodedPassword) {
        if (!pe.matches(rawPassword,encodedPassword)) {
            throw new UserException(INVALID_CREDENTIALS);
        }
    }

    public String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000)); // 0000~9999 범위
    }

    public void validateVerificationCode(String expectedVerificationCode, String actualVerificationCode,String phoneNumber) {
        checkVerificationCodeStatus(phoneNumber);
        checkIfVerificationCodeMismatch(expectedVerificationCode, actualVerificationCode);
    }

    /**
     *  인증 코드의 만료 여부 및 존재 여부 확인
     */
    public void checkVerificationCodeStatus(String phoneNumber) {
        VerificationCodeStatus status = redisService.getVerificationCodeStatus(phoneNumber);
        switch (status) {
            case EXPIRED:
                throw new AuthException(EXPIRED_VERIFICATION_CODE); // 410 GONE
            case NOT_FOUND:
                throw new AuthException(VERIFICATION_CODE_NOT_FOUND); // 404 NOT FOUND
        }
    }

    /**
     *  인증 코드가 일치하는지 확인
     */
    public void checkIfVerificationCodeMismatch(String expectedVerificationCode, String actualVerificationCode) {
        if (!expectedVerificationCode.equals(actualVerificationCode)) {
            throw new AuthException(MISMATCHED_VERIFICATION_CODE);
        }
    }

}
