package com.example.carnation.domain.payment.kakao.cqrs;

import com.example.carnation.common.exception.KakaoPaymentException;
import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import com.example.carnation.domain.payment.kakao.repository.KakaoPaymentReadyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.example.carnation.common.response.enums.KakaoPaymentApiResponseEnum.KAKAO_PAYMENT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class KakaoPaymentQuery {
    private final KakaoPaymentReadyRepository repository;

    @Transactional(readOnly = true)
    public KakaoPayment readById(Long id) {
        return repository.findById(id).orElseThrow(() -> new KakaoPaymentException(KAKAO_PAYMENT_NOT_FOUND));
    }

}
