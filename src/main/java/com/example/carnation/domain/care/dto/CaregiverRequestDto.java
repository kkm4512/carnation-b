package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병인 정보 요청 DTO") // DTO 전체 설명
public class CaregiverRequestDto {

    @Schema(description = "간병인의 이름", example = "이영희")
    @NotBlank(message = "이름은 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 20, message = "이름은 20자 이하로 입력해야 합니다.") // 이름 길이 제한
    private String name;

    @Schema(description = "키 (cm 단위)", example = "170.5")
    @NotNull(message = "키는 필수 입력 항목입니다.") // null 값 불가
    @Positive(message = "키는 양수여야 합니다.") // 0 이하 값 불가
    private Double height;

    @Schema(description = "몸무게 (kg 단위)", example = "65.3")
    @NotNull(message = "몸무게는 필수 입력 항목입니다.") // null 값 불가
    @Positive(message = "몸무게는 양수여야 합니다.") // 0 이하 값 불가
    private Double weight;

    @Schema(description = "간병인 매칭 공개 여부 (true: 공개, false: 비공개)", example = "true")
    @NotNull(message = "간병인 공개 여부는 필수 입력 항목입니다.")
    private Boolean isVisible;

}
