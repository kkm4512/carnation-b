package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.Caregiver;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(
        description = "간병인 요약 정보 DTO",
        example = "{ \"id\": 1, \"name\": \"이영희\", \"height\": 170.5, \"weight\": 65.3, \"isVisible\": true, \"createdAt\": \"2024-03-01T10:00:00\", \"updatedAt\": \"2024-03-01T10:00:00\" }"
)
public class CaregiverSimpleResponseDto {

    @Schema(description = "간병인 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "간병인의 이름", example = "이영희", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "키 (cm 단위)", example = "170.5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double height;

    @Schema(description = "몸무게 (kg 단위)", example = "65.3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double weight;

    @Schema(description = "간병인 매칭 공개 여부 (true: 공개, false: 비공개)", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isVisible;

    @Schema(description = "간병 매칭 가능 상태", example = "true")
    private Boolean isMatch;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00", nullable = true)
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-01T10:00:00", nullable = true)
    private LocalDateTime updatedAt;

    public static CaregiverSimpleResponseDto of(Caregiver entity) {
        return new CaregiverSimpleResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getHeight(),
                entity.getWeight(),
                entity.getIsVisible(),
                entity.getIsMatch(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
