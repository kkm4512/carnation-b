package com.example.carnation.domain.payment;

import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyRequestDto;
import com.example.carnation.domain.payment.kakao.dto.KakaoPaymentReadyResponseDto;
import com.example.carnation.domain.payment.kakao.entity.KakaoPayment;
import com.example.carnation.domain.user.MockUserTestInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MockPaymentInfo {

    /**
     * 필수값만 포함된 Mock 결제 정보 생성
     */
    public static KakaoPaymentReadyRequestDto getKakaoPaymentReadyRequestDto1() {
        return new KakaoPaymentReadyRequestDto(
                "TC0ONETIME", // 가맹점 코드
                "mock_order_123456", // 가맹점 주문번호
                "mock_user_98765", // 가맹점 회원 ID
                "테스트 상품", // 상품명
                1, // 상품 수량
                5000, // 상품 총액
                0 // 상품 비과세 금액
        );
    }

    public static KakaoPaymentReadyRequestDto getKakaoPaymentReadyRequestRequiredDtoFail1() {
        return new KakaoPaymentReadyRequestDto(
                "TC0ONETIME", // 가맹점 코드
                "mock_order_123456", // 가맹점 주문번호
                "mock_user_98765", // 가맹점 회원 ID
                "테스트 상품", // 상품명
                1, // 상품 수량
                null, // 상품 총액
                0 // 상품 비과세 금액
        );
    }

    /**
     * 카카오페이 결제 준비 API 응답 (필수값 + 선택값 포함)
     */
    public static KakaoPaymentReadyResponseDto getKakaoPaymentReadyResponse1() {
        return new KakaoPaymentReadyResponseDto(
                "T09876543210987654321", // 결제 고유 번호
                "https://mock.kakaopay.com/redirect/app", // 모바일 앱 리디렉트 URL
                "https://mock.kakaopay.com/redirect/mobile", // 모바일 웹 리디렉트 URL
                "https://mock.kakaopay.com/redirect/pc", // PC 웹 리디렉트 URL
                "kakaopay://app", // Android 앱 스킴
                "kakaopay://ios", // iOS 앱 스킴
                LocalDateTime.now()
        );
    }

    public static KakaoPayment getKakaoPaymentReady1() {
        return KakaoPayment.of(
                MockUserTestInfo.getUser1(),
                getKakaoPaymentReadyRequestDto1()
        );
    }

    public static HttpHeaders getHeadersByKakaoPayment() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + "testKey");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static Map<String, String> getParamsByKakaoPaymentReady1(KakaoPayment entity) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", entity.getCid());
        params.put("partner_order_id", entity.getPartnerOrderId());
        params.put("partner_user_id", entity.getPartnerUserId());
        params.put("item_name", entity.getItemName());
        params.put("quantity", String.valueOf(entity.getQuantity()));
        params.put("total_amount", String.valueOf(entity.getTotalAmount()));
        params.put("tax_free_amount", String.valueOf(entity.getTaxFreeAmount()));
        params.put("approval_url", "/test/api/v1/payment/kakao/callback/approval?kakao_payment_ready_id=" + entity.getId());
        params.put("cancel_url", "/test/api/v1/payment/kakao/callback/cancel?kakao_payment_ready_id=" + entity.getId());
        params.put("fail_url", "/test/api/v1/payment/kakao/callback/fail?kakao_payment_ready_id=" + entity.getId());

        if (entity.getCidSecret() != null) params.put("cid_secret", entity.getCidSecret());
        if (entity.getItemCode() != null) params.put("item_code", entity.getItemCode());
        if (entity.getVatAmount() != null) params.put("vat_amount", String.valueOf(entity.getVatAmount()));
        if (entity.getGreenDeposit() != null) params.put("green_deposit", String.valueOf(entity.getGreenDeposit()));
        if (entity.getPaymentMethod() != null) params.put("payment_method_type", entity.getPaymentMethod().name());
        if (entity.getInstallMonth() != null) params.put("install_month", String.valueOf(entity.getInstallMonth()));
        if (entity.getUseShareInstallment() != null) params.put("use_share_installment", entity.getUseShareInstallment());
        return params;
    }


    public static Map<String, String> getParamsByKakaoPaymentApproval1(KakaoPayment entity) {
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
