package com.example.carnation.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductRequestDto {
    @NotBlank
    @Schema(description = "상품명", example = "비타민C 영양제")
    String productName;

    @NotBlank
    @Schema(description = "상품 설명", example = "면역력을 높이는 비타민C 영양제")
    String description;

    @NotNull
    @Min(1)
    @Schema(description = "가격 (단위: 원)", example = "15000")
    Integer price;

    @NotNull
    @Min(0)
    @Schema(description = "재고 수량", example = "100")
    Integer quantity;

    @NotBlank
    @Schema(description = "카테고리", example = "건강보조식품")
    String category;

    @NotBlank
    @Schema(description = "브랜드명", example = "헬스케어 브랜드")
    String brand;
}