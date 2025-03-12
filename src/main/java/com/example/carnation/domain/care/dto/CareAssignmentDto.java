package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 배정 DTO") // DTO 전
public class CareAssignmentDto {
    @Schema(description = "간병 배정 ID", example = "1")
    private Long id;

    @Schema(description = "간병 배정 생성 시간", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "간병 배정 수정 시간", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    @Schema(description = "간병 배정 작성 사용자 (User)", example = "1")
    private User user;

    @Schema(description = "간병 배정 피간병인 (Patient)", example = "1")
    private Patient patient;

    @Schema(description = "간병 배정 간병인 (Caregiver)", example = "1")
    private Caregiver caregiver;

    public static CareAssignmentDto of(CareAssignment entity) {
        return new CareAssignmentDto(
                entity.getId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getUser(),
                entity.getPatient(),
                entity.getCaregiver()
        );
    }
}
