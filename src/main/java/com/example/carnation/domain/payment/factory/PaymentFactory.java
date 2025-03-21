package com.example.carnation.domain.payment.factory;

import com.example.carnation.common.exception.PaymentException;
import com.example.carnation.common.response.enums.PaymentApiResponseEnum;
import com.example.carnation.domain.payment.common.constans.PaymentGateway;
import com.example.carnation.domain.payment.impl.kakao.service.KakaoPaymentService;
import com.example.carnation.domain.payment.interfaces.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
    private final KakaoPaymentService kakaoPaymentService;

    public PaymentService getPaymentService(PaymentGateway paymentGateway) {
        return switch (paymentGateway) {
            case KAKAOPAY -> kakaoPaymentService;
            default -> throw new PaymentException(PaymentApiResponseEnum.PAYMENT_METHOD_NOT_SUPPORTED);
        };
    }
}
