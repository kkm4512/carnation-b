package com.example.carnation.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserTransferRequestDto {

    @Schema(description = "송금 금액", example = "10000")
    @NotNull(message = "금액은 필수 입력 항목입니다.")
    @Min(value = 1, message = "금액은 1원 이상이어야 합니다.")
    private Integer amount;

    @Schema(description = "송금 대상 ID", example = "12345")
    @NotNull(message = "대상 ID는 필수 입력 항목입니다.")
    private Long targetId;
}
