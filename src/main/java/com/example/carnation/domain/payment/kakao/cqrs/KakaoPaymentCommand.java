package com.example.carnation.domain.payment.kakao.cqrs;

import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import com.example.carnation.domain.payment.kakao.repository.KakaoPaymentReadyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class KakaoPaymentCommand {
    private final KakaoPaymentReadyRepository repository;

    @Transactional
    public KakaoPaymentReady create(KakaoPaymentReady entity) {
        return repository.save(entity);
    }
}
