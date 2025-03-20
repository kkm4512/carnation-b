package com.example.carnation.domain.payment.kakao.service;

import com.example.carnation.domain.payment.kakao.constans.KakaoPaymentStatus;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentCommand;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentQuery;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentApprovalResponseDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import com.example.carnation.domain.payment.kakao.helper.KakaoPaymentHelper;
import com.example.carnation.domain.product.cars.ProductQuery;
import com.example.carnation.domain.product.entity.Product;
import com.example.carnation.domain.user.common.cqrs.UserQuery;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
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
public class KakaoPaymentService {
    private final KakaoPaymentCommand kakaoPaymentCommand;
    private final KakaoPaymentQuery kakaoPaymentQuery;
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
    public KakaoPaymentReadyResponseDto ready(final AuthUser authUser, final Long productId) {
        try {
            Product product = productQuery.readById(productId);
            User user = userQuery.readById(authUser.getUserId());
            HttpHeaders headers = kakaoPaymentHelper.getHeadersByKakaoPayment();
            KakaoPayment kakaoPayment = KakaoPayment.of(user,product);
            KakaoPayment savedKakaoPayment = kakaoPaymentCommand.create(kakaoPayment);
            Map<String, String> params = kakaoPaymentHelper.getParamsByKakaoPayment(savedKakaoPayment);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

            log.info("📢 카카오페이 결제 요청 시작");
            log.info("🔹 요청 데이터: {}", params);

            KakaoPaymentReadyResponseDto resDto = restTemplate.exchange(KAKAO_READY_URL, HttpMethod.POST, entity, KakaoPaymentReadyResponseDto.class).getBody();
            savedKakaoPayment.updateTid(resDto.getTid());
            kakaoPaymentCommand.create(savedKakaoPayment);

            log.info("✅ 카카오페이 결제 준비 완료");
            log.info("🔹 결제 고유 번호(TID): {}", resDto.getTid());
            log.info("🔹 Redirect URL (PC): {}", resDto.getNextRedirectPcUrl());
            log.info("🔹 Redirect URL (Mobile): {}", resDto.getNextRedirectMobileUrl());
            log.info("🔹 Redirect URL (App): {}", resDto.getNextRedirectAppUrl());
            log.info("🔹 응답 생성 시간: {}", resDto.getCreatedAt());
            return resDto;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 카카오페이 결제 승인 (approve)
     */
    @Transactional
    public KakaoPaymentStatus approval(final Long kakaoPaymentReadyId,final String pgToken) {
        try {
            KakaoPayment kakaoPaymentReady = kakaoPaymentQuery.readById(kakaoPaymentReadyId);
            kakaoPaymentReady.updatePgToken(pgToken);
            final RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = kakaoPaymentHelper.getParamsByKakaoPaymentApproval(kakaoPaymentReady);
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
            log.info("🔹 카드 매입사: {}", response.getCard_info().getKakaopayPurchaseCorp());
            log.info("🔹 카드 발급사: {}", response.getCard_info().getKakaopayIssuerCorp());
            log.info("🔹 카드 타입: {}", response.getCard_info().getCardType());
            kakaoPaymentReady.updateStatus(KakaoPaymentStatus.APPROVED);
            return KakaoPaymentStatus.APPROVED;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public KakaoPaymentStatus cancel(final Long kakaoPaymentReadyId) {
        KakaoPayment kakaoPaymentReady = kakaoPaymentQuery.readById(kakaoPaymentReadyId);
        kakaoPaymentReady.updateStatus(KakaoPaymentStatus.CANCEL);
        return KakaoPaymentStatus.CANCEL;
    }


    @Transactional
    public KakaoPaymentStatus fail(final Long kakaoPaymentReadyId) {
        KakaoPayment kakaoPaymentReady = kakaoPaymentQuery.readById(kakaoPaymentReadyId);
        kakaoPaymentReady.updateStatus(KakaoPaymentStatus.FAIL);
        return KakaoPaymentStatus.FAIL;
    }


}
