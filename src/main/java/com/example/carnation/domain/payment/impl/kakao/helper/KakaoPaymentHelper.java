package com.example.carnation.domain.payment.impl.kakao.helper;

import com.example.carnation.domain.order.entity.Order;
import com.example.carnation.domain.payment.common.entity.Payment;
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

    public Map<String, String> getParamsByKakaoPayment(Order entity) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", entity.getPayment().getCid());
        params.put("partner_order_id", entity.getPayment().getPartnerOrderId());
        params.put("partner_user_id", entity.getPayment().getPartnerUserId());
        params.put("item_name", entity.getPayment().getItemName());
        params.put("quantity", String.valueOf(entity.getPayment().getQuantity()));
        params.put("total_amount", String.valueOf(entity.getPayment().getTotalAmount()));
        params.put("tax_free_amount", String.valueOf(entity.getPayment().getTaxFreeAmount()));
        params.put("approval_url", serverUrl + "/api/v1/payment/kakao/callback/approval?order_id=" + entity.getId());
        params.put("cancel_url", serverUrl + "/api/v1/payment/kakao/callback/cancel?order_id=" + entity.getId());
        params.put("fail_url", serverUrl + "/api/v1/payment/kakao/callback/fail?order_id=" + entity.getId());

        var payment = entity.getPayment(); // 가독성을 위해 지역 변수로 추출

        if (payment.getCidSecret() != null) params.put("cid_secret", payment.getCidSecret());
        if (payment.getItemCode() != null) params.put("item_code", payment.getItemCode());
        if (payment.getVatAmount() != null) params.put("vat_amount", String.valueOf(payment.getVatAmount()));
        if (payment.getGreenDeposit() != null) params.put("green_deposit", String.valueOf(payment.getGreenDeposit()));
        if (payment.getPaymentMethod() != null) params.put("payment_method_type", payment.getPaymentMethod().name());
        if (payment.getInstallMonth() != null) params.put("install_month", String.valueOf(payment.getInstallMonth()));
        if (payment.getUseShareInstallment() != null) params.put("use_share_installment", payment.getUseShareInstallment());

        return params;
    }



    public Map<String, String> getParamsByKakaoPaymentApproval(Payment entity) {
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
