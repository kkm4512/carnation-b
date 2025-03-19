package com.example.carnation.domain.user.oAuth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Schema(description = "OAuth로 부터 받아온 User Info")
public class OAuthUserDto {
    private String email;
    private String nickname;

    public static OAuthUserDto of(String email, String nickname) {
        return new OAuthUserDto(email, nickname);
    }
}
