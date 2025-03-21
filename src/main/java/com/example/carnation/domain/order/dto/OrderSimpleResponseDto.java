package com.example.carnation.domain.order.dto;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Schema(description = "주문 응답 DTO")
public class OrderSimpleResponseDto {

    private Long orderId;  // 주문 ID

    private PaymentGateway paymentGateway;

    private Boolean isPaid;     // 결제 상태

    private Integer quantity;  // 주문 수량

    private String address;  // 배송 주소

    private String receiverName;  // 수령인 이름

    public static OrderSimpleResponseDto of(Order order) {
        return OrderSimpleResponseDto.builder()
                .orderId(order.getId())
                .paymentGateway(order.getPaymentGateway())
                .isPaid(order.getIsPaid())
                .quantity(order.getQuantity())
                .address(order.getAddress())
                .receiverName(order.getReceiverName())
                .build();
    }

}
