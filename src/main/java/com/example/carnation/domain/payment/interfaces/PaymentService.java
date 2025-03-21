package com.example.carnation.domain.payment.interfaces;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.impl.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentResponseDto;
import com.example.carnation.domain.user.common.entity.User;

public interface PaymentService {

    KakaoPaymentResponseDto ready(User user, final Order order);
    KakaoPaymentStatus approval(final Long orderId, final String pgToken);
    KakaoPaymentStatus cancel(final Long orderId);
    KakaoPaymentStatus fail(final Long orderId);
}
