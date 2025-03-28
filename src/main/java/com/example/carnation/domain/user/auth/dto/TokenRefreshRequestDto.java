package com.example.carnation.domain.user.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "토큰 갱신 요청 DTO") // DTO의 설명 수정
public class TokenRefreshRequestDto {

    @NotBlank
    @Schema(description = "기존에 발급받은 Acceess Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNzQyNDczNDg4LCJpYXQiOjE3NDE4Njg2ODh9.6W0X_HuAyVlxH-71ghzLEcGHjG-Qzff1YeQuQYkUsgk")
    private String accessToken;

    @NotBlank
    @Schema(description = "기존에 발급받은 Refresh Token", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI0IiwiZXhwIjoxNzQyNDczNDg4LCJpYXQiOjE3NDE4Njg2ODh9.6W0X_HuAyVlxH-71ghzLEcGHjG-Qzff1YeQuQYkUsgk")
    private String refreshToken;
}
