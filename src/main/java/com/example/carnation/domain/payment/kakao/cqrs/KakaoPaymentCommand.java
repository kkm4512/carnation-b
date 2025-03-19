package com.example.carnation.domain.payment.kakao.cqrs;

import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import com.example.carnation.domain.payment.kakao.repository.KakaoPaymentReadyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KakaoPaymentCommand {
    private final KakaoPaymentReadyRepository repository;

    @Transactional
    public KakaoPayment create(KakaoPayment entity) {
        return repository.save(entity);
    }
}
