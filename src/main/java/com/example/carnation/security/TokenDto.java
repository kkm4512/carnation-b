package com.example.carnation.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "JWT Token Response DTO")
public class TokenDto {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long userId;

    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;

    public static TokenDto of(Long userId, String accessToken, String refreshToken) {
        return new TokenDto(
                userId,
                accessToken,
                refreshToken
        );
    }
}
