package com.example.carnation.domain.payment.common.repository;

import com.example.carnation.domain.payment.common.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
