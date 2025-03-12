package com.example.carnation.security;

import com.example.carnation.domain.user.constans.UserType;
import com.example.carnation.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "JWT 인증 및 사용자 정보를 담은 DTO")
public class JwtDto {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용자 닉네임", example = "카네이션123")
    private String nickname;

    @Schema(description = "사용자 이메일 주소", example = "user@example.com")
    private String email;

    @Schema(description = "사용자 역할", example = "ROLE_USER")
    private UserRole userRole;

    @Schema(description = "사용자 유형", example = "CAREGIVER")
    private UserType userType;

    public static JwtDto of(User user) {
        return new JwtDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole(),
                user.getUserType()
        );
    }
}