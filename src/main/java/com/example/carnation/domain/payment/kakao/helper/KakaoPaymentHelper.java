package com.example.carnation.domain.payment.kakao.helper;

import com.example.carnation.domain.payment.kakao.entity.KakaoPaymentReady;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoPaymentHelper {


    @Value("${payment.kakao.secret-key-dev}")
    private String kakaoPaymentSecretKey;

    @Value("${server.url}")
    private String serverUrl;

    public HttpHeaders getHeadersByKakaoPayment() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + kakaoPaymentSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public Map<String, String> getParamsByKakaoPaymentReady(KakaoPaymentReady entity) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", entity.getCid());
        params.put("partner_order_id", entity.getPartnerOrderId());
        params.put("partner_user_id", entity.getPartnerUserId());
        params.put("item_name", entity.getItemName());
        params.put("quantity", String.valueOf(entity.getQuantity()));
        params.put("total_amount", String.valueOf(entity.getTotalAmount()));
        params.put("tax_free_amount", String.valueOf(entity.getTaxFreeAmount()));
        params.put("approval_url", serverUrl + "/api/v1/payment/kakao/callback/approval?kakao_payment_ready_id=" + entity.getId());
        params.put("cancel_url", serverUrl +  "/api/v1/payment/kakao/callback/cancel?kakao_payment_ready_id=" + entity.getId());
        params.put("fail_url", serverUrl +  "/api/v1/payment/kakao/callback/fail?kakao_payment_ready_id=" + entity.getId());

        if (entity.getCidSecret() != null) params.put("cid_secret", entity.getCidSecret());
        if (entity.getItemCode() != null) params.put("item_code", entity.getItemCode());
        if (entity.getVatAmount() != null) params.put("vat_amount", String.valueOf(entity.getVatAmount()));
        if (entity.getGreenDeposit() != null) params.put("green_deposit", String.valueOf(entity.getGreenDeposit()));
        if (entity.getPaymentMethod() != null) params.put("payment_method_type", entity.getPaymentMethod().name());
        if (entity.getInstallMonth() != null) params.put("install_month", String.valueOf(entity.getInstallMonth()));
        if (entity.getUseShareInstallment() != null) params.put("use_share_installment", entity.getUseShareInstallment());
        return params;
    }


    public Map<String, String> getParamsByKakaoPaymentApproval(KakaoPaymentReady entity) {
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
