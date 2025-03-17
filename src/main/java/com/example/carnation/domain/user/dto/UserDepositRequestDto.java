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
public class UserDepositRequestDto {

    @Schema(description = "송금 금액", example = "10000")
    @NotNull(message = "금액은 필수 입력 항목입니다.")
    @Min(value = 1, message = "금액은 1원 이상이어야 합니다.")
    private Integer amount;

}
