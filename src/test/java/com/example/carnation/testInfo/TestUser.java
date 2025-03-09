package com.example.carnation.testInfo;

import com.example.carnation.domain.user.dto.req.SigninRequestDto;
import com.example.carnation.domain.user.dto.req.SignupRequestDto;

public class TestUser {
    public static SignupRequestDto getSignupRequestDto1() {
        return new SignupRequestDto(
                "test@naver.com1",
                "!@Skdud3401",
                "testNickname1"
        );
    }

    public static SigninRequestDto getSigninRequestDto1() {
        return new SigninRequestDto(
                "test@naver.com1",
                "!@Skdud3401"
        );
    }
}
