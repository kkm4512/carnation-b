package com.example.carnation.domain.care.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "간병 기록 요청 DTO") // DTO 전체 설명
public class CareHistoryRequestDto {

    @Schema(description = "간병 매칭 ID")
    @NotNull(message = "careMatchingId는 필수입니다.")
    @Positive(message = "careMatchingId는 0보다 커야 합니다.")
    private Long careMatchingId;

    @Schema(description = "간병 내용", example = "환자에게 식사를 제공하고 산책을 도왔음")
    @Size(max = 500, message = "간병 내용은 최대 500자까지 입력할 수 있습니다.") // 최대 500자 제한
    private String text;
}
