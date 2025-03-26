package com.example.carnation.domain.payment.common.cqrs;

import com.example.carnation.common.exception.KakaoPaymentException;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.common.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.KakaoPaymentApiResponseEnum.KAKAO_PAYMENT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class PaymentQuery {
    private final PaymentRepository repository;

    @Transactional(readOnly = true)
    public Payment readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new KakaoPaymentException(KAKAO_PAYMENT_NOT_FOUND));
    }

}
