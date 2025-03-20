package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.constans.CareMatchingStatus;
import com.example.carnation.domain.care.entity.CareMatching;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 매칭 응답 DTO") // DTO 전
public class CareMatchingDetailResponse {
    @Schema(description = "간병 매칭 ID", example = "1")
    private Long id;

    @Schema(description = "간병 매칭 상태", example = "PENDING")
    private CareMatchingStatus matchStatus;

    @Column(nullable = false)
    @Schema(description = "간병인 -> 환자 Or 환자 -> 간병인에게 청구할 비용", example = "10000")
    private Integer amount;

    @Schema(description = "간병 매칭 생성 시간", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "간병 매칭 수정 시간", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "간병 매칭 환자 (Patient)", example = "1")
    private PatientSimpleResponseDto patientSimpleResponseDto;

    @Schema(description = "간병 매칭 간병인 (Caregiver)", example = "1")
    private CaregiverSimpleResponseDto caregiverSimpleResponseDto;

    public static CareMatchingDetailResponse of(CareMatching entity) {
        return new CareMatchingDetailResponse(
                entity.getId(),
                entity.getMatchStatus(),
                entity.getAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                PatientSimpleResponseDto.of(entity.getPatient()),
                CaregiverSimpleResponseDto.of(entity.getCaregiver())
        );
    }
}
