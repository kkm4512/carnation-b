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
@Schema(description = "간병 기록 응답 DTO")
public class CareHistoryResponseDto {

    @Schema(description = "간병 기록 ID", example = "1")
    private Long id;

    @Schema(description = "간병 내용", example = "환자에게 식사를 제공하고 산책을 도왔음")
    private String text;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-01T10:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "S3에 저장된 미디어 리스트")
    private List<CareMediaResponse> medias;

    public static CareHistoryResponseDto of(CareHistory entity) {
        return new CareHistoryResponseDto(
                entity.getId(),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                CareMediaResponse.of(entity.getMedias())
        );
    }
}
