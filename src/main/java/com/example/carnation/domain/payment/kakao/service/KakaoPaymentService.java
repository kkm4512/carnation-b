package com.example.carnation.domain.payment.kakao.service;

import com.example.carnation.domain.payment.interfaces.PaymentService;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentCommand;
import com.example.carnation.domain.payment.kakao.cqrs.KakaoPaymentQuery;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentApprovalResponseDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import com.example.carnation.domain.user.cqrs.UserQuery;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoPaymentService implements PaymentService {
    private final KakaoPaymentCommand kakaoPaymentCommand;
    private final KakaoPaymentQuery kakaoPaymentQuery;
    private final UserQuery userQuery;
    private final RestTemplate restTemplate;
    private final String KAKAO_READY_URL = "https://open-api.kakaopay.com/online/v1/payment/ready";
    private final String KAKAO_APPROVE_URL = "https://open-api.kakaopay.com/online/v1/payment/approve";


    @Value("${payment.kakao.secret-key-dev}")
    private String kakaoPaymentSecretKey;

    @Value("${server.url}")
    private String serverUrl;

    /**
     * 카카오페이 결제 준비 (ready)
     */
    public KakaoPaymentReadyResponseDto ready(AuthUser authUser, KakaoPaymentReadyRequestDto reqDto) {
        try {
            User user = userQuery.readById(authUser.getUserId());
            // HTTP 요청 헤더
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "SECRET_KEY " + kakaoPaymentSecretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            KakaoPaymentReady kakaoPaymentReady = KakaoPaymentReady.of(user,reqDto);
            KakaoPaymentReady savedKakaoPaymentReady = kakaoPaymentCommand.create(kakaoPaymentReady);
            Map<String, String> params = getParamsByKakaoPaymentReady(savedKakaoPaymentReady);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(params, headers);

            // 요청 데이터 로깅
            log.info("📢 카카오페이 결제 요청 시작");
            log.info("🔹 요청 데이터: {}", params);

            // API 요청
            KakaoPaymentReadyResponseDto resDto = restTemplate.exchange(KAKAO_READY_URL, HttpMethod.POST, entity, KakaoPaymentReadyResponseDto.class).getBody();
            savedKakaoPaymentReady.updateTid(resDto.getTid());
            savedKakaoPaymentReady.updateCreatedAt(resDto.getCreatedAt());
            kakaoPaymentCommand.create(savedKakaoPaymentReady);

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
    public KakaoPaymentApprovalResponseDto approval(Long kakaoPaymentReadyId,String pgToken) {
        try {
            KakaoPaymentReady kakaoPaymentReady = kakaoPaymentQuery.readById(kakaoPaymentReadyId);
            kakaoPaymentReady.updatePgToken(pgToken);
            final RestTemplate restTemplate = new RestTemplate();
            Map<String, String> params = getParamsByKakaoPaymentApproval(kakaoPaymentReady);

            // HTTP 요청 헤더
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "SECRET_KEY " + kakaoPaymentSecretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
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
            return response;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    private Map<String, String> getParamsByKakaoPaymentReady(KakaoPaymentReady entity) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", entity.getCid());
        params.put("partner_order_id", entity.getPartnerOrderId());
        params.put("partner_user_id", entity.getPartnerUserId());
        params.put("item_name", entity.getItemName());
        params.put("quantity", String.valueOf(entity.getQuantity()));
        params.put("total_amount", String.valueOf(entity.getTotalAmount()));
        params.put("tax_free_amount", String.valueOf(entity.getTaxFreeAmount()));
        params.put("approval_url", serverUrl + "/api/v1/payment/kakao/callback/approval?kakao_payment_ready_id=" + entity.getId());
        params.put("cancel_url", serverUrl +  "/api/v1/payment/kakao/callback/cancel");
        params.put("fail_url", serverUrl +  "/api/v1/payment/kakao/callback/fail");

        if (entity.getCidSecret() != null) params.put("cid_secret", entity.getCidSecret());
        if (entity.getItemCode() != null) params.put("item_code", entity.getItemCode());
        if (entity.getVatAmount() != null) params.put("vat_amount", String.valueOf(entity.getVatAmount()));
        if (entity.getGreenDeposit() != null) params.put("green_deposit", String.valueOf(entity.getGreenDeposit()));
        if (entity.getPaymentMethodType() != null) params.put("payment_method_type", entity.getPaymentMethodType().name());
        if (entity.getInstallMonth() != null) params.put("install_month", String.valueOf(entity.getInstallMonth()));
        if (entity.getUseShareInstallment() != null) params.put("use_share_installment", entity.getUseShareInstallment());
        return params;
    }


    private Map<String, String> getParamsByKakaoPaymentApproval(KakaoPaymentReady entity) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", entity.getCid());
        params.put("tid", entity.getTid());
        params.put("partner_order_id", entity.getPartnerOrderId());
        params.put("partner_user_id", entity.getPartnerUserId());
        params.put("pg_token", entity.getPgToken());
        if (entity.getCidSecret() != null) params.put("cid_secret", entity.getCidSecret());
        if (entity.getTotalAmount() != null) params.put("total_amount", String.valueOf(entity.getTotalAmount()));
        return params;
    }


}
