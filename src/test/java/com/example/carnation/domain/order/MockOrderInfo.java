package com.example.carnation.domain.order;

import com.example.carnation.domain.order.dto.OrderRequestDto;
import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;

import static com.example.carnation.domain.product.MockProductInfo.getProduct1;

public class MockOrderInfo {

    public static Order getOrder1() {
        OrderRequestDto dto = new OrderRequestDto(
                getProduct1().getId(),
                PaymentGateway.KAKAOPAY,
                2,
                "서울시 강남구 테헤란로 123",
                "문 앞에 놔주세요",
                "WELCOME10"
        );
        return Order.of(getProduct1(), dto);
    }
}
