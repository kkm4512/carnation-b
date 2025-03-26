package com.example.carnation.domain.payment.common.cqrs;

import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.common.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentCommand {
    private final PaymentRepository repository;

    @Transactional
    public Payment create(Payment entity) {
        return repository.save(entity);
    }
}
