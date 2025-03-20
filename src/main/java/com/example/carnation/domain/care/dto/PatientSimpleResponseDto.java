package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간단한 환자 정보 DTO (기본 정보만 제공)")
public class PatientSimpleResponseDto {

    @Schema(description = "환자 ID", example = "1")
    private Long id;

    @Schema(description = "환자 이름", example = "김철수")
    private String name;

    @Schema(description = "환자가 있는 장소", example = "서울특별시 강남구 삼성동")
    private String location;

    @Schema(description = "환자의 병명", example = "고혈압, 당뇨병")
    private String diagnosis;

    @Schema(description = "환자 공개 여부 (매칭 시스템에서 노출 여부)", example = "true")
    private Boolean isVisible;

    @Schema(description = "간병 매칭 가능 상태", example = "true")
    private Boolean isMatch;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    public static PatientSimpleResponseDto of(Patient entity) {
        return new PatientSimpleResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getLocation(),
                entity.getDiagnosis(),
                entity.getIsVisible(),
                entity.getIsMatch(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
