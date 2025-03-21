package com.example.carnation.domain.order.cqrs;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCommand {
    private final OrderRepository repository;

    public Order create(Order entity) {
        return repository.save(entity);
    }
}
