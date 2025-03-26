package com.example.carnation.domain.payment.impl.kakao.service;

import com.example.carnation.domain.order.cqrs.OrderQuery;
import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.entity.Payment;
import com.example.carnation.domain.payment.impl.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.payment.impl.kakao.cqrs.PaymentCommand;
import com.example.carnation.domain.payment.impl.kakao.cqrs.PaymentQuery;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentApprovalResponseDto;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.impl.kakao.dto.KakaoPaymentSimpleResponseDto;
import com.example.carnation.domain.payment.impl.kakao.helper.KakaoPaymentHelper;
import com.example.carnation.domain.payment.interfaces.PaymentService;
import com.example.carnation.domain.product.cqrs.ProductQuery;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoPaymentService implements PaymentService {
    private final PaymentCommand paymentCommand;
    private final PaymentQuery PaymentQuery;
    private final OrderQuery orderQuery;
    private final UserQuery userQuery;
    private final RestTemplate restTemplate;
    private final ProductQuery productQuery;
    private final KakaoPaymentHelper kakaoPaymentHelper;
    private final String KAKAO_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private final String KAKAO_APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";

    /**
     * 카카오페이 결제 준비 (ready)
     */
    @Transactional
    public KakaoPaymentSimpleResponseDto ready(final User user, final Order order) {
        try {
            HttpHeaders headers = kakaoPaymentHelper.getHeadersByKakaoPayment();
            Payment payment = Payment.of(user,order);
            Payment savedPayment = paymentCommand.create(payment);
            order.updatePayment(savedPayment);
            Map<String, String> params = kakaoPaymentHelper.getParamsByKakaoPayment(order);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

            log.info("📢 카카오페이 결제 요청 시작");
            log.info("🔹 요청 데이터: {}", params);

            KakaoPaymentReadyResponseDto resDto = restTemplate.exchange(KAKAO_READY_URL, HttpMethod.POST, entity, KakaoPaymentReadyResponseDto.class).getBody();
            savedPayment.updateTid(resDto.getTid());


            log.info("✅ 카카오페이 결제 준비 완료");
            log.info("🔹 결제 고유 번호(TID): {}", resDto.getTid());
            log.info("🔹 Redirect URL (PC): {}", resDto.getNextRedirectPcUrl());
            log.info("🔹 Redirect URL (Mobile): {}", resDto.getNextRedirectMobileUrl());
            log.info("🔹 Redirect URL (App): {}", resDto.getNextRedirectAppUrl());
            log.info("🔹 응답 생성 시간: {}", resDto.getCreatedAt());
            return KakaoPaymentSimpleResponseDto.of(resDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 카카오페이 결제 승인 (approve)
     */
    @Transactional
    public KakaoPaymentStatus approval(final Long orderId,final String pgToken) {
        try {
            Order order = orderQuery.readById(orderId);
            order.getPayment().updatePgToken(pgToken);
            final RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = kakaoPaymentHelper.getParamsByKakaoPaymentApproval(order.getPayment());
            HttpHeaders headers = kakaoPaymentHelper.getHeadersByKakaoPayment();
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

            log.info("📢 카카오페이 결제 승인 요청 시작");
            log.info("🔹 요청 데이터: {}", params);

            // API 요청
            KakaoPaymentApprovalResponseDto response = restTemplate.exchange(KAKAO_APPROVE_URL, HttpMethod.POST, entity, KakaoPaymentApprovalResponseDto.class).getBody();

            log.info("✅ 카카오페이 결제 승인 완료");
            log.info("🔹 결제 고유 번호(TID): {}", response.getTid());
            log.info("🔹 결제 승인 금액: {}원", response.getAmount().getTotal());
            log.info("🔹 주문자 ID: {}", response.getPartner_user_id());
            log.info("🔹 가맹점 코드(CID): {}", response.getCid());
            order.getPayment().updateStatus(KakaoPaymentStatus.APPROVED);
            order.updateIsPaid(true);
            return KakaoPaymentStatus.APPROVED;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public KakaoPaymentStatus cancel(final Long orderId) {
        Order order = orderQuery.readById(orderId);
        order.getPayment().updateStatus(KakaoPaymentStatus.CANCEL);
        return KakaoPaymentStatus.CANCEL;
    }


    @Transactional
    public KakaoPaymentStatus fail(final Long orderId) {
        Order order = orderQuery.readById(orderId);
        order.getPayment().updateStatus(KakaoPaymentStatus.FAIL);
        return KakaoPaymentStatus.FAIL;
    }


}
