package com.example.carnation.domain.user.auth.dto;

import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 DTO") // DTO 설명 추가
public class UserSimpleResponseDto {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 닉네임 (고유값, 필수)", example = "카네이션123")
    private String nickname;

    @Schema(description = "사용자 이메일 (고유값, 필수)", example = "nayoyn440@naerv.com")
    private String email;

    @Schema(description = "사용자 역할 (ROLE_ADMIN, ROLE_USER 등)", example = "ROLE_USER")
    private UserRole userRole;

    public static UserSimpleResponseDto of(User user) {
        return new UserSimpleResponseDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getUserRole()
        );
    }


}
