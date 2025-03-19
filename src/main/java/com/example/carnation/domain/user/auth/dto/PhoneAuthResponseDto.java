package com.example.carnation.domain.user.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthResponseDto {
    private String phoneNumber;
    private String verificationCode;

    public static PhoneAuthResponseDto of(String phoneNumber, String verificationCode) {
        return new PhoneAuthResponseDto(
                phoneNumber,
                verificationCode
        );
    }
}
