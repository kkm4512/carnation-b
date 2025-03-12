package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 기록 파일 요청 DTO") // DTO 전체 설명
public class CareHistoryFilesRequestDto {

    @ArraySchema(schema = @Schema(type = "string", format = "binary"))
    @Schema(description = "업로드할 이미지 파일 리스트")
    private List<MultipartFile> imageFiles;

    @ArraySchema(schema = @Schema(type = "string", format = "binary"))
    @Schema(description = "업로드할 동영상 파일 리스트")
    private List<MultipartFile> videoFiles;

    public static CareHistoryFilesRequestDto of(List<MultipartFile> imageFiles, List<MultipartFile> videoFiles) {
        return new CareHistoryFilesRequestDto(
                imageFiles != null ? imageFiles : new ArrayList<>(),
                videoFiles != null ? videoFiles : new ArrayList<>()
        );
    }

}
