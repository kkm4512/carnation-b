package com.example.carnation.domain.product.dto;

import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.auth.dto.UserSimpleResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductResponseDto {

    @Schema(description = "상품 ID", example = "1")
    Long id;

    @Schema(description = "상품명", example = "비타민C 영양제")
    String name;

    @Schema(description = "상품 설명", example = "면역력을 높이는 비타민C 영양제")
    String description;

    @Schema(description = "가격 (단위: 원)", example = "15000")
    Integer price;

    @Schema(description = "재고 수량", example = "100")
    Integer quantity;

    @Schema(description = "카테고리", example = "건강보조식품")
    String category;

    @Schema(description = "브랜드명", example = "헬스케어 브랜드")
    String brand;

    @Schema(description = "판매 가능 여부", example = "true")
    Boolean isAvailable;

    @Schema(description = "상품 등록자", example = "나김밥")
    UserSimpleResponseDto userSimpleResponseDto;

    @Schema(description = "생성일", example = "2024-03-19T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "수정일", example = "2024-03-20T15:30:00")
    private LocalDateTime updatedAt;

    public static ProductResponseDto of(Product entity) {
        return new ProductResponseDto(
                entity.getId(),
                entity.getProductName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getCategory(),
                entity.getBrand(),
                entity.getIsAvailable(),
                UserSimpleResponseDto.of(entity.getUser()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}