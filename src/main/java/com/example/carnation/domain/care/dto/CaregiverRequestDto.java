package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.constans.RelationshipType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병인 정보 요청 DTO") // DTO 전체 설명
public class CaregiverRequestDto {

    @Schema(description = "간병인의 이름", example = "이영희")
    @NotBlank(message = "이름은 필수 입력 항목입니다.") // 빈 값 불가
    @Size(max = 20, message = "이름은 20자 이하로 입력해야 합니다.") // 이름 길이 제한
    private String name;

    @Schema(description = "주민등록번호 (13자리)", example = "900101-2345678")
    @NotBlank(message = "주민등록번호는 필수 입력 항목입니다.") // 빈 값 불가
    @Pattern(
            regexp = "^(\\d{6})-(\\d{7})$",
            message = "주민등록번호 형식이 올바르지 않습니다. (예: 900101-2345678)"
    ) // 6자리-7자리 숫자 형식 검증
    private String residentRegistrationNumber;

    @Schema(description = "키 (cm 단위)", example = "170.5")
    @NotNull(message = "키는 필수 입력 항목입니다.") // null 값 불가
    @Positive(message = "키는 양수여야 합니다.") // 0 이하 값 불가
    private Double height;

    @Schema(description = "몸무게 (kg 단위)", example = "65.3")
    @NotNull(message = "몸무게는 필수 입력 항목입니다.") // null 값 불가
    @Positive(message = "몸무게는 양수여야 합니다.") // 0 이하 값 불가
    private Double weight;

    @Schema(description = "피간병인과의 관계", example = "FRIEND") // Enum 값으로 입력
    @NotNull(message = "피간병인과의 관계는 필수 입력 항목입니다.") // 빈 값 불가
    private RelationshipType relationship;

    @Schema(description = "간병인의 연락처", example = "010-1234-5678")
    @NotBlank(message = "연락처는 필수 입력 항목입니다.") // 빈 값 불가
    @Pattern(
            regexp = "^(010|011|016|017|018|019)-\\d{3,4}-\\d{4}$",
            message = "연락처 형식이 올바르지 않습니다. (예: 010-1234-5678)"
    ) // 휴대폰 번호 형식 검증
    private String phoneNumber;

    @Schema(
            description = "간병 시작 날짜",
            example = "2024-03-01T10:00:00"
    )
    @NotNull(message = "간병 시작 날짜는 필수 입력 항목입니다.")
    private LocalDateTime startDateTime;

    @Schema(
            description = "간병 종료 날짜",
            example = "2024-03-05T10:00:00"
    )
    @NotNull(message = "간병 종료 날짜는 필수 입력 항목입니다.")
    private LocalDateTime endDateTime;
}
