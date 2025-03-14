package com.example.carnation.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthRequestDto {

    @Schema(description = "사용자의 휴대폰 번호 (숫자만 입력)", example = "01012345678")
    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(01[016789])\\d{3,4}\\d{4}$", message = "유효한 휴대폰 번호를 입력해주세요.")
    private String phoneNumber;
}
