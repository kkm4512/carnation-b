package com.example.carnation.domain.order.cqrs;

import com.example.carnation.common.exception.OrderException;
import com.example.carnation.common.response.enums.OrderApiResponseEnum;
import com.example.carnation.domain.order.dto.OrderSearchDto;
import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.order.repository.OrderRepository;
import com.example.carnation.domain.user.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQuery {
    private final OrderRepository repository;

    public Page<Order> readPageMe(User user, Pageable pageable, OrderSearchDto dto) {
        return repository.findAllMe(user.getId(),dto.getPaymentGateway(), dto.getIsPaid(), pageable);
    }

    public Order readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new OrderException(OrderApiResponseEnum.ORDER_NOT_FOUND));
    }
}
