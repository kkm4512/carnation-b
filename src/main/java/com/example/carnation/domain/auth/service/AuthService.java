package com.example.carnation.domain.auth.service;

import com.example.carnation.annotation.SaveVerificationCode;
import com.example.carnation.common.exception.AuthException;
import com.example.carnation.common.service.RedisService;
import com.example.carnation.common.service.constans.VerificationCodeStatus;
import com.example.carnation.domain.auth.dto.PhoneAuthRequestDto;
import com.example.carnation.domain.auth.dto.PhoneAuthResponseDto;
import com.example.carnation.domain.auth.dto.PhoneAuthVerifyRequestDto;
import com.example.carnation.domain.auth.helper.SmsHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.example.carnation.common.response.enums.AuthApiResponse.*;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AuthService")
public class AuthService {
    private final SmsHelper smsHelper;
    private final RedisService redisService;

    @SaveVerificationCode
    public PhoneAuthResponseDto sendVerificationCode(final PhoneAuthRequestDto dto) {
        String verificationCode = generateVerificationCode(); // 4자리 랜덤 숫자 생성
        smsHelper.sendSms(dto.getPhoneNumber(), verificationCode);
        return PhoneAuthResponseDto.of(dto.getPhoneNumber(), verificationCode);
    }

    public void verifyCode(final PhoneAuthVerifyRequestDto dto) {
        String expectedVerificationCode = dto.getVerificationCode();
        String actualVerificationCode = redisService.getVerificationCode(dto.getPhoneNumber());
        validateVerificationCode(expectedVerificationCode, actualVerificationCode, dto.getPhoneNumber());
    }

    private String generateVerificationCode() {
        return String.format("%04d", new Random().nextInt(10000)); // 0000~9999 범위
    }

    private void validateVerificationCode(String expectedVerificationCode, String actualVerificationCode,String phoneNumber) {
        checkVerificationCodeStatus(phoneNumber);
        checkIfVerificationCodeMismatch(expectedVerificationCode, actualVerificationCode);
    }

    /**
     *  인증 코드의 만료 여부 및 존재 여부 확인
     */
    private void checkVerificationCodeStatus(String phoneNumber) {
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
    private void checkIfVerificationCodeMismatch(String expectedVerificationCode, String actualVerificationCode) {
        if (!expectedVerificationCode.equals(actualVerificationCode)) {
            throw new AuthException(MISMATCHED_VERIFICATION_CODE);
        }
    }


}
