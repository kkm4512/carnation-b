package com.example.carnation.domain.care.dto;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.domain.care.constans.CareMatchingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "매칭 상태를 포함한 페이징 및 정렬 요청 DTO")
public class CareMatchingStatusSearchDto extends PageSearchDto {

    @Schema(description = "검색할 매칭 상태 (예: PENDING, MATCHING, END)", example = "PENDING")
    private CareMatchingStatus matchStatus;

}
