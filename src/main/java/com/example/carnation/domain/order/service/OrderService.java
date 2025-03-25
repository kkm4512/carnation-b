package com.example.carnation.domain.order.service;

import com.example.carnation.common.dto.PageSearchDto;
import com.example.carnation.domain.order.cqrs.OrderCommand;
import com.example.carnation.domain.order.cqrs.OrderQuery;
import com.example.carnation.domain.order.dto.OrderRequestDto;
import com.example.carnation.domain.order.dto.OrderSearchDto;
import com.example.carnation.domain.order.dto.OrderSimpleResponseDto;
import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.factory.PaymentFactory;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentResponseDto;
import com.example.carnation.domain.payment.interfaces.PaymentService;
import com.example.carnation.domain.product.cqrs.ProductQuery;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderCommand orderCommand;
    private final OrderQuery orderQuery;
    private final PaymentFactory paymentFactory;
    private final ProductQuery productQuery;
    private final UserQuery userQuery;

    @Transactional
    public KakaoPaymentResponseDto generate(AuthUser authUser, OrderRequestDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Product product = productQuery.readById(dto.getProductId());
        user.validateIsMe(product.getUser());
        product.decreaseStock(dto.getQuantity());
        Order order = Order.of(product, dto);
        Order savedOrder = orderCommand.create(order);
        PaymentService paymentService = paymentFactory.getPaymentService(order.getPaymentGateway());
        return paymentService.ready(user, savedOrder);
    }

    public Page<OrderSimpleResponseDto> findPageMe(AuthUser authUser, OrderSearchDto dto) {
        User user = userQuery.readById(authUser.getUserId());
        Pageable pageable = PageSearchDto.of(dto);
        Page<Order> result = orderQuery.readPageMe(user, pageable, dto);
        return result.map(OrderSimpleResponseDto::of);
    }
}
