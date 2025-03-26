package com.example.carnation.domain.payment.interfaces;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.dto.PaymentResponseDto;
import com.example.carnation.domain.payment.impl.kakao.constans.PaymentStatus;
import com.example.carnation.domain.user.common.entity.User;

public interface PaymentService {

    PaymentResponseDto ready(User user, final Order order);
    PaymentStatus approval(final Long orderId, final String pgToken);
    PaymentStatus cancel(final Long orderId);
    PaymentStatus fail(final Long orderId);

}
