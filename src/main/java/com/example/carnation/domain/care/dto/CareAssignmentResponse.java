package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.user.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 배정 응답 DTO") // DTO 전
public class CareAssignmentResponse {
    @Schema(description = "간병 배정 ID", example = "1")
    private Long id;

    @Schema(description = "간병 배정 생성 시간", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "간병 배정 수정 시간", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "간병 배정 작성 사용자 (User)", example = "1")
    private UserDto userDto;

    @Schema(description = "간병 배정 피간병인 (Patient)", example = "1")
    private PatientDto patientDto;

    @Schema(description = "간병 배정 간병인 (Caregiver)", example = "1")
    private CaregiverDto caregiverDto;

    public static CareAssignmentResponse of(CareAssignment entity) {
        return new CareAssignmentResponse(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                UserDto.of(entity.getUser()),
                PatientDto.of(entity.getPatient()),
                CaregiverDto.of(entity.getCaregiver())
        );
    }
}
