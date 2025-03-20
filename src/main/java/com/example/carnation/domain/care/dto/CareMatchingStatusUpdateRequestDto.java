package com.example.carnation.domain.care.dto;

import com.example.carnation.domain.care.constans.CareMatchingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "매칭 상태 변경 요청 DTO")  // 전체 DTO 설명
public class CareMatchingStatusUpdateRequestDto {

    @Schema(description = "변경할 매칭 상태 (예: PENDING, MATCHING, END, CANCEL)", example = "MATCHING")
    private CareMatchingStatus matchStatus;

    private Long careMatchingId;

    public static CareMatchingStatusUpdateRequestDto of(CareMatchingStatus matchStatus, Long careMatchingId) {
        return new CareMatchingStatusUpdateRequestDto(matchStatus, careMatchingId);
    }
}
