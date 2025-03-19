package com.example.carnation.domain.product.entity;

import com.example.carnation.domain.product.dto.ProductResponseDto;
import com.example.carnation.domain.user.common.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Schema(description = "헬스케어 상품 엔티티")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "상품명", example = "비타민C 영양제")
    private String name;

    @Column(nullable = false, length = 255)
    @Schema(description = "상품 설명", example = "면역력을 높이는 비타민C 영양제")
    private String description;

    @Column(nullable = false)
    @Schema(description = "가격 (단위: 원)", example = "15000")
    private Integer price;

    @Column(nullable = false)
    @Schema(description = "재고 수량", example = "100")
    private Integer quantity;

    @Column(nullable = false, length = 50)
    @Schema(description = "카테고리", example = "건강보조식품")
    private String category;

    @Column(nullable = false, length = 50)
    @Schema(description = "브랜드명", example = "헬스케어 브랜드")
    private String brand;

    @Column(nullable = false)
    @Schema(description = "판매 가능 여부", example = "true")
    private Boolean isAvailable;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Schema(description = "생성일", example = "2024-03-19T12:00:00")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    @Schema(description = "수정일", example = "2024-03-20T15:30:00")
    private LocalDateTime updatedAt;

    // ✅ User와 ManyToOne 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // 외래 키 설정
    private User user;

    public Product(User user, String name, String description, Integer price, Integer quantity, String category, String brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.brand = brand;
        this.isAvailable = true;
        this.user = user;
    }

    public static Product of(User user, ProductResponseDto dto) {
        return new Product(
                user,
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getQuantity(),
                dto.getCategory(),
                dto.getBrand()
        );
    }

}
