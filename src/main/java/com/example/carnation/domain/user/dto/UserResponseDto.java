package com.example.carnation.domain.user.dto;

import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 DTO") // DTO 설명 추가
public class UserResponseDto {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 닉네임 (고유값, 필수)", example = "카네이션123")
    private String nickname;

    @Schema(description = "사용자 이메일 (고유값, 필수)", example = "nayoyn440@naerv.com")
    private String email;

    @Schema(description = "사용자 역할 (ROLE_ADMIN, ROLE_USER 등)", example = "ROLE_USER")
    private UserRole userRole;

    @Schema(description = "생성 날짜 (자동 입력)", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "마지막 수정 날짜 (자동 입력)", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    public static UserResponseDto of(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
