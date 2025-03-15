package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 매칭 요청 DTO") // DTO 설명 추가
public class CareMatchingRequestDto {

    @Schema(description = "매칭할 간병인의 ID", example = "1")
    @NotNull(message = "간병인 ID는 필수 입력 항목입니다.")
    private Long caregiverId;

    @Schema(description = "매칭할 환자의 ID", example = "1")
    @NotNull(message = "환자 ID는 필수 입력 항목입니다.")
    private Long patientId;

    @Schema(description = "간병 시작 시간", example = "2024-03-20T08:00:00")
    @NotNull(message = "시작 시간은 공백일 수 없습니다.")
    @FutureOrPresent(message = "시작 시간은 현재 시간 이후여야 합니다.")
    private LocalDateTime startDateTime;

    @Schema(description = "간병 종료 시간", example = "2024-03-20T20:00:00")
    @NotNull(message = "종료 시간은 공백일 수 없습니다.")
    @FutureOrPresent(message = "종료 시간은 현재 시간 이후여야 합니다.")
    private LocalDateTime endDateTime;
}
