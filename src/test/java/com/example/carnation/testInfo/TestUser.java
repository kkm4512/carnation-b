package com.example.carnation.testInfo;

import com.example.carnation.domain.user.dto.SigninRequestDto;
import com.example.carnation.domain.user.dto.SignupRequestDto;

public class TestUser {
    public static SignupRequestDto getSignupRequestDto1() {
        return new SignupRequestDto(
                "test@naver.com1",
                "!@Skdud3401",
                "testNickname1",
                null,
                null
        );
    }

    public static SigninRequestDto getSigninRequestDto1() {
        return new SigninRequestDto(
                "test@naver.com1",
                "!@Skdud3401"
        );
    }
}
