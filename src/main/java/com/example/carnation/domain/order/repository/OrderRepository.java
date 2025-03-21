package com.example.carnation.domain.order.repository;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o " +
            "WHERE o.product.user.id = :userId " +
            "AND (:paymentGateway IS NULL OR o.paymentGateway = :paymentGateway) " +
            "AND (:isPaid IS NULL OR o.isPaid = :isPaid)")
    Page<Order> findAllMe(Long userId, PaymentGateway paymentGateway, boolean isPaid, Pageable pageable);
}
