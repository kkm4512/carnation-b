package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.constans.MediaType;
import com.example.carnation.domain.care.entity.CareMedia;
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
@Schema(description = "간병 미디어 DTO")
public class CareMediaResponse {

    @Schema(description = "미디어 ID", example = "1")
    private Long id;

    @Schema(description = "S3에 저장된 파일 URL", example = "https://s3.example.com/file1.jpg")
    private String fileUrl;

    @Schema(description = "저장된 파일명", example = "file1.jpg")
    private String fileName;

    @Schema(description = "원본 파일명", example = "original_file_name.jpg")
    private String fileOriginName;

    @Schema(description = "파일 크기 (bytes)", example = "204800")
    private String fileSize;

    @Schema(description = "파일 타입 (IMAGE 또는 VIDEO)", example = "IMAGE")
    private MediaType mediaType;

    @Schema(description = "생성일시", example = "2024-03-01T10:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-03-02T15:30:00")
    private LocalDateTime updatedAt;

    public static CareMediaResponse of(CareMedia entity) {
        return new CareMediaResponse(
            entity.getId(),
            entity.getFileAbsolutePath(),
            entity.getFileName(),
            entity.getFileOriginName(),
            entity.getFileSize(),
            entity.getMediaType(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static List<CareMediaResponse> of (List<CareMedia> entities) {
        return entities.stream().map(CareMediaResponse::of).toList();
    }
}
