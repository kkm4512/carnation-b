package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.entity.CareHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 기록 DTO")
public class CareHistoryDto {

    @Schema(description = "간병 기록 ID", example = "1")
    private Long id;

    @Schema(description = "간병 내용", example = "환자에게 식사를 제공하고 산책을 도왔음")
    private String text;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "간병인", example = "1")
    private CaregiverDto caregiverDto;

    @Schema(description = "S3에 저장된 미디어 URL 리스트")
    private List<CareMediaDto> medias;

    public static CareHistoryDto of(CareHistory entity) {
        return new CareHistoryDto(
                entity.getId(),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                CaregiverDto.of(entity.getCaregiver()),
                CareMediaDto.of(entity.getMedias())
        );
    }

    public static List<CareHistoryDto> of(List<CareHistory> entities) {
        return entities.stream().map(CareHistoryDto::of).toList();
    }
}
