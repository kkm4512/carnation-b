package com.example.carnation.domain.order.dto;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "주문 검색 필터 DTO")
public class OrderSearchDto extends PageSearchDto {

    @Schema(description = "결제 여부 (true: 결제 완료, false: 미결제)", example = "true")
    private Boolean isPaid;

    @Schema(description = "결제 게이트웨이 (예: KAKAOPAY)", example = "KAKAOPAY")
    private PaymentGateway paymentGateway;
}
