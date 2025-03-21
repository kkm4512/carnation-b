package com.example.carnation.domain.order.dto;

import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "주문 요청 DTO")
public class OrderRequestDto {

    @NotNull
    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @NotNull
    @Schema(description = "결제 게이트웨이 (예: KAKAOPAY)", example = "KAKAOPAY")
    private PaymentGateway paymentGateway;

    @NotNull
    @Schema(description = "주문 수량", example = "2")
    private Integer quantity;

    @NotNull
    @Schema(description = "배송 주소", example = "서울특별시 강남구 역삼동 123-45")
    private String address;

    @Schema(description = "배송 시 요청사항", example = "부재 시 문 앞에 놓아주세요")
    private String orderMemo;

    @Schema(description = "할인 쿠폰 코드", example = "WELCOME10")
    private String couponCode;
}
