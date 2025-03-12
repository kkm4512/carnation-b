package com.example.carnation.domain.user.dto;

import com.example.carnation.domain.user.constans.UserType;
import com.example.carnation.security.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 회원가입 요청 DTO") // DTO 설명 추가
public class SignupRequestDto {

    @Schema(description = "사용자의 이메일 주소", example = "test@naver.com1")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Schema(
            description = "사용자의 비밀번호. 최소 8자 이상, 최대 20자 이하. " +
                    "대문자 1개 이상, 숫자 1개 이상, 특수문자 2개 이상 포함해야 함.",
            example = "!@Skdud3401"
    )
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[@$!%*?&].*[@$!%*?&])(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "비밀번호는 최소 하나의 대문자, 최소 두 개의 특수문자, 최소 하나의 숫자를 포함해야 합니다."
    )
    private String password;

    @Schema(
            description = "사용자의 닉네임. 3자 이상, 15자 이하. " +
                    "영문, 숫자, 한글만 허용됨.",
            example = "root123"
    )
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "닉네임은 3자 이상 15자 이하로 입력해야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "닉네임은 영문, 숫자, 한글만 허용됩니다.")
    private String nickname;

    @Schema(description = "사용자 역할 (ROLE_USER, ROLE_ADMIN 등)", example = "ROLE_USER", nullable = true, defaultValue = "ROLE_USER")
    private UserRole userRole;

    @Schema(description = "사용자 유형 (CAREGIVER, PATIENT)", example = "CAREGIVER", nullable = true, defaultValue = "CAREGIVER")
    private UserType userType;
}
