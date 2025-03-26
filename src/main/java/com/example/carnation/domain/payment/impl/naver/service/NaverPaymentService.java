package com.example.carnation.domain.payment.impl.naver.service;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.cqrs.PaymentCommand;
import com.example.carnation.domain.payment.common.dto.PaymentResponseDto;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.impl.kakao.constans.PaymentStatus;
import com.example.carnation.domain.payment.impl.naver.dto.NaverPaymentResponseDto;
import com.example.carnation.domain.payment.impl.naver.helper.NaverPaymentHelper;
import com.example.carnation.domain.payment.interfaces.PaymentService;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.init.PropertyInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverPaymentService implements PaymentService {
    private final NaverPaymentHelper naverPaymentHelper;
    private final PaymentCommand paymentCommand;
    private final RestTemplate restTemplate;
    private final String NAVER_PAYMENT_READY_URL = "https://dev-pub.apis.naver.com/" + PropertyInfo.PAYMENT_NAVER_MERCHANT_ID + "/naverpay/payments/v2/reserve";

    @Override
    public PaymentResponseDto ready(User user, Order order) {
        HttpHeaders headers = naverPaymentHelper.getHeadersByNaverPayment();
        Payment payment = Payment.of(user,order);
        Payment savedPayment = paymentCommand.create(payment);
        order.updatePayment(savedPayment);
        Map<String, String> params = naverPaymentHelper.getParamsByNaverPayment(order);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

        log.info("📢 네이버페이 결제 요청 시작");
        log.info("🔹 요청 데이터: {}", params);

        NaverPaymentResponseDto resDto = restTemplate.exchange(NAVER_PAYMENT_READY_URL, HttpMethod.POST, entity, NaverPaymentResponseDto.class).getBody();

        log.info("✅ 네이버페이 결제 준비 완료");
        log.info("🔹 code: {}", resDto.getCode());
        log.info("🔹 message: {}", resDto.getMessage());
        log.info("🔹 body: {}", resDto.getBody());
        return PaymentResponseDto.of(resDto);
    }

    public PaymentStatus approval(Long orderId, String pgToken) {
        System.out.println(orderId);
        return null;
    }

    public PaymentStatus cancel(Long orderId) {
        return null;
    }

    public PaymentStatus fail(Long orderId) {
        return null;
    }
}
