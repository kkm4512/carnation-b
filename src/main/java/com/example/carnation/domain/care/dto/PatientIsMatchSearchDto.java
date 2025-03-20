package com.example.carnation.domain.care.dto;

import com.example.carnation.common.dto.PageSearchDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "환자의 매칭 상태를 포함한 페이징 및 정렬 요청 DTO")
public class PatientIsMatchSearchDto extends PageSearchDto {

    @Schema(description = "검색할 매칭 상태 (예: true = 매칭 가능한 간병인, false = 매칭 불가능한 간병인)", example = "PENDING")
    private Boolean isMatch;

}
