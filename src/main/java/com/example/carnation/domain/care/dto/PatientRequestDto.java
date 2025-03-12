package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "피간병인 정보 요청 DTO") // DTO 전체 설명
public class PatientRequestDto {

    @Schema(description = "피간병인의 이름", example = "김철수")
    @NotBlank(message = "이름은 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 20, message = "이름은 30자 이하로 입력해야 합니다.") // 이름 길이 제한
    private String name;

    @Schema(description = "주민등록번호 (13자리)", example = "800101-1234567")
    @NotBlank(message = "주민등록번호는 필수 입력 항목입니다.") // 빈 값 불가
    @Pattern(
            regexp = "^(\\d{6})-(\\d{7})$",
            message = "주민등록번호 형식이 올바르지 않습니다. (예: 800101-1234567)"
    ) // 6자리-7자리 숫자 형식 검증
    private String residentRegistrationNumber;
}
