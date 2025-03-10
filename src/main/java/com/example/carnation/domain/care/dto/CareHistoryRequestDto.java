package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병인 기록 작성 요청 DTO") // DTO 전체 설명
public class CareHistoryRequestDto {
    @Schema(description = "간병 내용", example = "환자에게 식사를 제공하고 산책을 도왔음")
    @Size(max = 500, message = "간병 내용은 최대 500자까지 입력할 수 있습니다.") // 최대 500자 제한
    private String text;

    @Schema(description = "업로드할 이미지 파일 리스트", type = "string", format = "binary")
    private List<MultipartFile> imageFiles;

    @Schema(description = "업로드할 동영상 파일 리스트", type = "string", format = "binary")
    private List<MultipartFile> videoFiles;

}
