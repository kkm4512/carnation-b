package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "환자 정보 요청 DTO") // DTO 전체 설명
public class PatientRequestDto {

    @Schema(description = "환자의 이름", example = "김철수")
    @NotBlank(message = "이름은 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 30, message = "이름은 30자 이하로 입력해야 합니다.") // 이름 길이 제한 수정
    private String name;

    @Schema(description = "환자가 있는 장소", example = "서울특별시 강남구 삼성동")
    @NotBlank(message = "장소는 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 100, message = "장소는 100자 이하로 입력해야 합니다.") // 주소 길이 제한
    private String location;

    @Schema(description = "환자의 병명", example = "고혈압, 당뇨병")
    @NotBlank(message = "병명은 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 100, message = "병명은 100자 이하로 입력해야 합니다.") // 병명 길이 제한
    private String diagnosis;

    @Schema(description = "환자 매칭 공개 여부 (true: 공개, false: 비공개)", example = "true")
    @NotNull(message = "환자 공개 여부는 필수 입력 항목입니다.")
    private Boolean isVisible;
}
