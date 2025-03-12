package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "피간병인 정보 Dto")
public class PatientDto {

    @Schema(description = "환자 ID", example = "1")
    private Long id;

    @Schema(description = "환자 이름", example = "김철수")
    private String name;

    @Schema(description = "주민등록번호 (14자리)", example = "850101-2345678")
    private String residentRegistrationNumber;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    public static PatientDto of(Patient entity) {
        return new PatientDto(
                entity.getId(),
                entity.getName(),
                entity.getResidentRegistrationNumber(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

}
